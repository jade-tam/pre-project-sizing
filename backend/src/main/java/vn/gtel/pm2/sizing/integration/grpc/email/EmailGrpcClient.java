package vn.gtel.pm2.sizing.integration.grpc.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vn.gtel.pm2.email.grpc.v1.EmailRequest;
import vn.gtel.pm2.email.grpc.v1.EmailResponse;
import vn.gtel.pm2.email.grpc.v1.EmailServiceGrpc;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailGrpcClient {

    private final EmailServiceGrpc.EmailServiceBlockingStub emailServiceBlockingStub;

    public void sendEmail(EmailRequest emailRequest) {
        log.info("Sending email to {} from email service grpc", emailRequest.getTo());

        EmailResponse emailResponse = emailServiceBlockingStub.sendEmail(emailRequest);

        log.info("Email sent with response status: {}", emailResponse.getStatus());
    }
}
