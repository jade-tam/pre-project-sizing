package vn.gtel.pm2.sizing.config.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.gtel.pm2.sizing.constant.RabbitMQConstants;

@Configuration
public class UserMessagingConfig {

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange(
                RabbitMQConstants.User.EXCHANGE
        );
    }

    // Queues ----------------------------------------------------------
    @Bean
    public Queue emailNotificationQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.Queue.EMAIL_NOTIFICATION)
                .deadLetterExchange(RabbitMQConstants.DeadLetter.EXCHANGE)
                .deadLetterRoutingKey(RabbitMQConstants.DeadLetter.RoutingKey.EMAIL_NOTIFICATION_FAILED)
                .build();
    }

    @Bean
    public Queue pushNotificationQueue() {
        return QueueBuilder
                .durable(RabbitMQConstants.Queue.PUSH_NOTIFICATION)
                .build();
    }

    // Bindings ---------------------------------------------------------
    @Bean
    public Binding emailNotificationQueueBinding() {
        return BindingBuilder
                .bind(emailNotificationQueue())
                .to(userExchange())
                .with(RabbitMQConstants.User.RoutingKey.REGISTERED);
    }

    @Bean
    public Binding pushNotificationQueueBinding() {
        return BindingBuilder
                .bind(pushNotificationQueue())
                .to(userExchange())
                .with(RabbitMQConstants.User.RoutingKey.REGISTERED);
    }
}