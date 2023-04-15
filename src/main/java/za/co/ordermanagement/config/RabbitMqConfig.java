package za.co.ordermanagement.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import za.co.ordermanagement.utils.PropertyProvider;

@Component
public class RabbitMqConfig {

    private final PropertyProvider propertyProvider;


    public RabbitMqConfig(PropertyProvider propertyProvider) {
        this.propertyProvider = propertyProvider;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(propertyProvider.getRabbitMqUsername());
        factory.setPassword(propertyProvider.getRabbitMqPassword());
        factory.setVirtualHost(propertyProvider.getRabbitMqVirtualHost());
        factory.setHost(propertyProvider.getRabbitMqHost());
        factory.setPassword(propertyProvider.getRabbitMqPassword());
        factory.setAutomaticRecoveryEnabled(true);
        return factory;
    }
}
