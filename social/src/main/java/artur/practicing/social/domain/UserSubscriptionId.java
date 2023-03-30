package artur.practicing.social.domain;


import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable  // сущность будет использоваться в качестве встраиваемой в другие сущности
public class UserSubscriptionId implements Serializable {
    @JsonView(Views.Id.class)
    private String channelId;
    @JsonView(Views.Id.class)
    private String subscriberId;

    public UserSubscriptionId() {
    }

    public UserSubscriptionId(String channelId, String subscriberId) {
        this.channelId = channelId;
        this.subscriberId = subscriberId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }
}