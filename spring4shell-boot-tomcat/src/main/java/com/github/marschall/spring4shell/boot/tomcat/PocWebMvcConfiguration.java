package com.github.marschall.spring4shell.boot.tomcat;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class PocWebMvcConfiguration {

  @Bean
  public PocController simpleController() {
    return new PocController();
  }

}
