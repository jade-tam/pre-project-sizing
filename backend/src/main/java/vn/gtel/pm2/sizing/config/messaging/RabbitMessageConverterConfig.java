package vn.gtel.pm2.sizing.config.messaging;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.gtel.pm2.sizing.messaging.event.UserRegisteredEvent;

@Configuration
public class RabbitMessageConverterConfig {

    @Bean
    public MessageConverter messageConverter() {
        JacksonJsonMessageConverter converter = new JacksonJsonMessageConverter();

        // For some reason, each even need to be added to trusted packages
        converter.getJavaTypeMapper().addTrustedPackages(UserRegisteredEvent.class.getPackageName());

        return converter;
    }
}