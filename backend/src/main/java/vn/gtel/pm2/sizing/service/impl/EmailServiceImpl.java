package vn.gtel.pm2.sizing.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.gtel.pm2.sizing.integration.grpc.email.EmailGrpcClient;
import vn.gtel.pm2.sizing.integration.rest.email.EmailFeignClient;
import vn.gtel.pm2.sizing.integration.rest.email.dto.request.EmailRequest;
import vn.gtel.pm2.sizing.messaging.event.UserRegisteredEvent;
import vn.gtel.pm2.sizing.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final EmailFeignClient emailClient;
    private final EmailGrpcClient emailGrpcClient;

    @Override
    public void sendWelcomeEmail(
            UserRegisteredEvent event
    ) {
        // send email using rest client
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

        // Send email using grpc client
        vn.gtel.pm2.email.grpc.v1.EmailRequest grpcEmailRequest = vn.gtel.pm2.email.grpc.v1.EmailRequest.newBuilder()
                .setTo(event.getEmail())
                .setSubject("Welcome to our platform from grpc")
                .setBody("Hello from grpc").build();
        emailGrpcClient.sendEmail(grpcEmailRequest);
    }
}