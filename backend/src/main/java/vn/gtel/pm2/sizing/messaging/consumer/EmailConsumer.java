package vn.gtel.pm2.sizing.messaging.consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vn.gtel.pm2.sizing.constant.RabbitMQConstants;
import vn.gtel.pm2.sizing.messaging.event.UserRegisteredEvent;
import vn.gtel.pm2.sizing.service.EmailService;

@Component
@RequiredArgsConstructor
@RabbitListener(
        queues = RabbitMQConstants.Queue.EMAIL_NOTIFICATION
)
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitHandler
    public void handle(UserRegisteredEvent event) {
        emailService.sendWelcomeEmail(event);
    }
}