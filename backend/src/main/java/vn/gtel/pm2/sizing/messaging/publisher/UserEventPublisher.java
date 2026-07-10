package vn.gtel.pm2.sizing.messaging.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import vn.gtel.pm2.sizing.constant.RabbitMQConstants;
import vn.gtel.pm2.sizing.messaging.event.UserRegisteredEvent;

@Service
@RequiredArgsConstructor
public class UserEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishUserRegistered(UserRegisteredEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConstants.User.EXCHANGE,
                RabbitMQConstants.User.Route.REGISTERED,
                event
        );
    }
}