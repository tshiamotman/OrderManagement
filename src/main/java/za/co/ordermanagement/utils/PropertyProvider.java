package za.co.ordermanagement.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertyProvider {

    @Value("${spring.rabbitmq.host}")
    private String rabbitMqHost;

    @Value("${spring.rabbitmq.port}")
    private int rabbitMqPort;

    @Value("${spring.rabbitmq.virtual-host}")
    private String rabbitMqVirtualHost;

    @Value("${spring.rabbitmq.username}")
    private String rabbitMqUsername;

    @Value("${spring.rabbitmq.password}")
    private String rabbitMqPassword;

    @Value("${messaging.service}")
    private String messagingServiceUrl;

    public String getMessagingServiceUrl() {
        return messagingServiceUrl;
    }

    public void setMessagingServiceUrl(String messagingServiceUrl) {
        this.messagingServiceUrl = messagingServiceUrl;
    }

    public String getRabbitMqPassword() {
        return rabbitMqPassword;
    }

    public void setRabbitMqPassword(String rabbitMqPassword) {
        this.rabbitMqPassword = rabbitMqPassword;
    }

    public String getRabbitMqUsername() {
        return rabbitMqUsername;
    }

    public void setRabbitMqUsername(String rabbitMqUsername) {
        this.rabbitMqUsername = rabbitMqUsername;
    }

    public String getRabbitMqVirtualHost() {
        return rabbitMqVirtualHost;
    }

    public void setRabbitMqVirtualHost(String rabbitMqVirtualHost) {
        this.rabbitMqVirtualHost = rabbitMqVirtualHost;
    }

    public int getRabbitMqPort() {
        return rabbitMqPort;
    }

    public void setRabbitMqPort(int rabbitMqPort) {
        this.rabbitMqPort = rabbitMqPort;
    }

    public String getRabbitMqHost() {
        return rabbitMqHost;
    }

    public void setRabbitMqHost(String rabbitMqHost) {
        this.rabbitMqHost = rabbitMqHost;
    }
}
