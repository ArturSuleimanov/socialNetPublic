package artur.practicing.social.service;


import artur.practicing.social.domain.User;
import artur.practicing.social.domain.UserSubscription;
import artur.practicing.social.repo.UserDetailsRepo;
import artur.practicing.social.repo.UserSubscriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    private final UserDetailsRepo userDetailsRepo;


    private final UserSubscriptionRepo userSubscriptionRepo;

    @Autowired
    public ProfileService(UserDetailsRepo userDetailsRepo, UserSubscriptionRepo userSubscriptionRepo) {
        this.userDetailsRepo = userDetailsRepo;
        this.userSubscriptionRepo = userSubscriptionRepo;
    }

    public User changeSubscription(User channel, DefaultOidcUser subscriber) {

        User subscriberFromDb = userDetailsRepo.findById(subscriber.getName()).orElseThrow();


        Set<UserSubscription> subscriptions1 = channel.getSubscribers();

        List<UserSubscription> subscriptions = channel.getSubscribers()
                .stream()
                .filter(subscription -> {
                            User currentUser = subscription.getSubscriber();
                            return currentUser.getId().equals(subscriberFromDb.getId());
                        }
                )
                .collect(Collectors.toList());


        if (subscriptions.isEmpty()) {
            UserSubscription subscription = new UserSubscription(channel, subscriberFromDb);
            channel.getSubscribers().add(subscription);
        } else {
            channel.getSubscribers().removeAll(subscriptions);
        }
        return userDetailsRepo.save(channel);
    }

    public List<UserSubscription> getSubscribers(User channel) {
        return userSubscriptionRepo.findByChannel(channel);
    }

    public UserSubscription changeSubscriptionStatus(DefaultOidcUser channel, User subscriber) {
        User channelFromDb = userDetailsRepo.findById(channel.getName()).orElseThrow();

        UserSubscription subscription = userSubscriptionRepo.findByChannelAndSubscriber(channelFromDb, subscriber);
        subscription.setActive(!subscription.isActive());

        return userSubscriptionRepo.save(subscription);
    }
}
