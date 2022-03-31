package com.github.marschall.spring4shell.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

import com.github.marschall.spring4shell.controller.PocWebMvcConfiguration;


@Import(PocWebMvcConfiguration.class)
@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
public class PocApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(PocApplication.class, args);
  }

}
