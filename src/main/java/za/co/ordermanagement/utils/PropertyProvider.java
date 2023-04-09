package za.co.ordermanagement.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyProvider {

    @Value("${redis.url}")
    private String redisUrl;

    @Value("${messaging.service}")
    private String messagingServiceUrl;

    public String getRedisUrl() {
        return redisUrl;
    }

    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }

    public String getMessagingServiceUrl() {
        return messagingServiceUrl;
    }

    public void setMessagingServiceUrl(String messagingServiceUrl) {
        this.messagingServiceUrl = messagingServiceUrl;
    }
}
