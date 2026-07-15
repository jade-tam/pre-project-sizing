package vn.gtel.pm2.sizing.integration.email;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class EmailFeignConfig {

    private final EmailProperties properties;

    @Bean
    RequestInterceptor apiKeyInterceptor() {
        return template ->
                template.header("X-API-KEY", properties.apiKey());
    }
}