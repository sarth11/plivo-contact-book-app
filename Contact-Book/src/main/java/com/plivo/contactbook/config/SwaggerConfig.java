package com.plivo.contactbook.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	@Bean
    public Docket contactApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()                 
                .apis(RequestHandlerSelectors.basePackage("com.plivo.contactbook.controller"))
                .build()
                .apiInfo(apiInfo()).securitySchemes(Collections.singletonList(securityScheme()));
    }
	
	private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("PLIVO CRUD REST API", "PLIVO REST API for contact-book web app", "1.0", "Terms of service",
                new Contact("Sarthak Soni", "https://www.linkedin.com/in/sarthak-soni-3630b9b9/", ""), "", "");
        return apiInfo;
    }

    private SecurityScheme securityScheme() {
		return new BasicAuth("basicAuth");
	}
}