package com.github.marschall.spring4shell.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
public class PocConfiguration {

  @Bean
  public SimpleController simpleController() {
    return new SimpleController();
  }

}
