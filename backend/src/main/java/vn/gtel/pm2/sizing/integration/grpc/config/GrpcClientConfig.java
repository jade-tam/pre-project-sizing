package vn.gtel.pm2.sizing.integration.grpc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.ImportGrpcClients;
import vn.gtel.pm2.email.grpc.v1.EmailServiceGrpc;

@Configuration
@ImportGrpcClients(
    target = "email-service",
    types = {
        EmailServiceGrpc.EmailServiceBlockingStub.class
    }
)
public class GrpcClientConfig {

}