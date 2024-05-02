package com.bezkoder.spring.swagger.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

  private final String devUrl;
  private final String prodUrl;

  public OpenAPIConfig(@Value("${bezkoder.openapi.dev-url}") String devUrl, @Value("${bezkoder.openapi.prod-url}") String prodUrl) {
    this.devUrl = devUrl;
    this.prodUrl = prodUrl;
  }

  @Bean
  public OpenAPI myOpenAPI() {
    var devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

    var prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("Server URL in Production environment");

    var contact = new Contact();
    contact.setEmail("bezkoder@gmail.com");
    contact.setName("BezKoder");
    contact.setUrl("https://www.bezkoder.com");

    var mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    var info = new Info()
        .title("Tutorial Management API")
        .version("1.0")
        .contact(contact)
        .description("This API exposes endpoints to manage tutorials.").termsOfService("https://www.bezkoder.com/terms")
        .license(mitLicense);

    return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
  }
}
