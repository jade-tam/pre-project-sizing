package vn.gtel.pm2.sizing.integration.email;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import vn.gtel.pm2.sizing.integration.email.dto.request.EmailRequest;
import vn.gtel.pm2.sizing.integration.email.dto.response.EmailResponse;

@Component
public class EmailClient {

    private final RestClient restClient;

    public EmailClient(
            RestClient.Builder builder,
            EmailProperties properties
    ) {

        this.restClient = builder
                .baseUrl(properties.baseUrl())
                .defaultHeader(
                        "X-API-KEY",
                        properties.apiKey()
                )
                .build();
    }

    public EmailResponse send(
            EmailRequest request
    ) {

        return restClient.post()
                .uri("/emails")
                .body(request)
                .retrieve()
                .body(EmailResponse.class);
    }
}