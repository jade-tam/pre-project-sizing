package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.gtel.pm2.sizing.integration.email.EmailClient;
import vn.gtel.pm2.sizing.integration.email.dto.request.EmailRequest;
import vn.gtel.pm2.sizing.messaging.event.UserRegisteredEvent;
import vn.gtel.pm2.sizing.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailClient emailClient;

    @Override
    public void sendWelcomeEmail(
            UserRegisteredEvent event
    ) {

        EmailRequest request =
                new EmailRequest();

        request.setTo(event.getEmail());
        request.setSubject(
                "Welcome to our platform"
        );

        request.setHtmlBody(
                "<h1>Hello "
                        + event.getFullName()
                        + "</h1>"
        );


        emailClient.send(request);
    }
}