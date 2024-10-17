package com.Globant.E_commerce.Util;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI openApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce API")
                        .version("0.0.1")
                        .description("API for a simple e-commerce application developed with java and spring-boot")
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org/"))
                );
    }
}