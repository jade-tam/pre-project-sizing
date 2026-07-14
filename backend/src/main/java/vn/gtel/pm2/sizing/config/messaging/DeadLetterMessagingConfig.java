package vn.gtel.pm2.sizing.config.messaging;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.gtel.pm2.sizing.constant.RabbitMQConstants;

@Configuration
public class DeadLetterMessagingConfig {

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(
                RabbitMQConstants.DeadLetter.EXCHANGE
        );
    }

    @Bean
    public Queue emailNotificationDlq() {
        return QueueBuilder
                .durable(RabbitMQConstants.Queue.EMAIL_NOTIFICATION_DLQ)
                .build();
    }

    @Bean
    public Binding emailNotificationDlqBinding() {
        return BindingBuilder
                .bind(emailNotificationDlq())
                .to(deadLetterExchange())
                .with(RabbitMQConstants.DeadLetter.RoutingKey.EMAIL_NOTIFICATION_FAILED);
    }
}