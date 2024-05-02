package com.bezkoder.spring.swagger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.bezkoder.spring.swagger"})
public class SpringBootSwagger3ExampleApplication {
  public static void main(String[] args) {
    SpringApplication.run(SpringBootSwagger3ExampleApplication.class, args);
  }
}
