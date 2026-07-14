package vn.gtel.pm2.sizing.integration.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import vn.gtel.pm2.sizing.integration.email.EmailProperties;

@Configuration
@EnableConfigurationProperties({
        EmailProperties.class
})
public class IntegrationConfig {

}