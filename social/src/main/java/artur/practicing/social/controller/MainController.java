package artur.practicing.social.controller;

import artur.practicing.social.domain.User;
import artur.practicing.social.domain.Views;
import artur.practicing.social.dto.MessagePageDto;
import artur.practicing.social.repo.UserDetailsRepo;
import artur.practicing.social.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {

    private final ObjectWriter messageWriter;
    private final ObjectWriter profileWriter;
    private final MessageService messageService;
    private final UserDetailsRepo userDetailsRepo;



    @Value("${spring.profiles.active}")
    private String profile;

    @Autowired
    public MainController(UserDetailsRepo userDetailsRepo, MessageService messageService, ObjectMapper mapper) {
        this.messageService = messageService;

        ObjectMapper objectMapper = mapper
                .setConfig(mapper.getSerializationConfig());
        this.messageWriter = objectMapper.writerWithView(Views.FullMessage.class);
        this.profileWriter = objectMapper.writerWithView(Views.FullProfile.class);

        this.userDetailsRepo = userDetailsRepo;
    }

    @GetMapping
    public String main(
            Model model,
            @AuthenticationPrincipal DefaultOidcUser user
    ) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();
        User userFromDb = null;
        if (user != null) {
            userFromDb = userDetailsRepo.findById(user.getName()).orElseThrow();
            //        Authentication auth = SecurityContextHolder.getContext().getAuthentication()
            String serializedProfile = profileWriter.writeValueAsString(userFromDb);
            model.addAttribute("profile", serializedProfile);

            Sort sort = Sort.by(Sort.Direction.DESC, "id");
            PageRequest pageRequest = PageRequest.of(0, MessageController.MESSAGES_PER_PAGE, sort);
            MessagePageDto messagePageDto = messageService.findForUser(pageRequest, userFromDb);
            String messages = messageWriter.writeValueAsString(messagePageDto.getMessages());
            model.addAttribute("messages", messages);

            data.put("currentPage", messagePageDto.getCurrentPage());
            data.put("totalPages", messagePageDto.getTotalPages());
        } else {
            model.addAttribute("messages", "[]");
            model.addAttribute("profile", "null");
        }

        model.addAttribute("frontendData", data);
        model.addAttribute("isDevMode", "dev".equals(profile));
        return "index";
    }
}
