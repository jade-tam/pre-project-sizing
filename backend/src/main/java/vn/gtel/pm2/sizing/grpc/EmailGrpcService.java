package vn.gtel.pm2.sizing.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.grpc.server.service.GrpcService;
import vn.gtel.pm2.email.grpc.v1.EmailRequest;
import vn.gtel.pm2.email.grpc.v1.EmailResponse;
import vn.gtel.pm2.email.grpc.v1.EmailServiceGrpc;


@GrpcService
@Slf4j
public class EmailGrpcService
        extends EmailServiceGrpc.EmailServiceImplBase {

    @Override
    public void sendEmail(
            EmailRequest request,
            StreamObserver<EmailResponse> responseObserver
    ) {

        log.info("Sending email to {}", request.getTo());

        // Pretend sending email...

        EmailResponse response =
                EmailResponse.newBuilder()
                        .setId("email-001")
                        .setStatus("SUCCESS")
                        .build();


        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}