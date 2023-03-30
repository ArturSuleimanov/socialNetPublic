package artur.practicing.social.config;


import artur.practicing.social.domain.User;
import artur.practicing.social.repo.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.SecurityFilterChain;

import java.time.LocalDateTime;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {

    @Autowired
    UserDetailsRepo userDetailsRepo;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                    .authorizeHttpRequests()
                    .requestMatchers("/", "/login**", "/js/**", "/error")
                    .permitAll()
                    .anyRequest().authenticated()
                .and()
                    .oauth2Login(oauth2 -> oauth2
                            .userInfoEndpoint(userInfo -> userInfo
                                    .oidcUserService(this.oidcUserService())
                            ))
                    .logout().logoutSuccessUrl("/").permitAll();
        return http.build();
    }

    private OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcUserService delegate = new OidcUserService();
        return (userRequest) -> {
            OidcUser oidcUser = delegate.loadUser(userRequest);
            String id = (String) oidcUser.getAttributes().get("sub");
            User user = userDetailsRepo.findById(id).orElseGet(() -> {
                User newUser = new User();
                newUser.setId(id);
                newUser.setName(oidcUser.getFullName());
                newUser.setEmail(oidcUser.getEmail());
                newUser.setGender(oidcUser.getGender());
                newUser.setLocale(oidcUser.getLocale());
                newUser.setUserpic(oidcUser.getPicture());
                return newUser;
            });
            user.setLastVisit(LocalDateTime.now());
            userDetailsRepo.save(user);
            return oidcUser;
        };
    }

}
