package vn.gtel.pm2.sizing.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.gtel.pm2.sizing.constant.AppConstants;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Pre Project Sizing API")
                        .version("v1")
                        .description("API documentation for Pre Project Sizing"))
                // Define a new security component
                .components(new Components()
                        .addSecuritySchemes(
                                AppConstants.SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}
