package vn.gtel.pm2.sizing.integration.email;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import vn.gtel.pm2.sizing.integration.email.dto.request.EmailRequest;
import vn.gtel.pm2.sizing.integration.email.dto.response.EmailResponse;

@FeignClient(
        name = "email-client",
        url = "${integration.email.base-url}",
        configuration = EmailFeignConfig.class
)
public interface EmailFeignClient {

    @PostMapping("/emails")
    EmailResponse send(@RequestBody EmailRequest request);
}
