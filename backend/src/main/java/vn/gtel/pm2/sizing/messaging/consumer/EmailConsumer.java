package vn.gtel.pm2.sizing.messaging.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import vn.gtel.pm2.sizing.constant.RabbitMQConstants;
import vn.gtel.pm2.sizing.messaging.event.UserRegisteredEvent;

@Component
public class EmailConsumer {

    @RabbitListener(
            queues = RabbitMQConstants.Queue.EMAIL_NOTIFICATION
    )
    public void handle(UserRegisteredEvent event) {

        System.out.println(
                "Sending welcome email to "
                        + event.email()
        );
    }
}