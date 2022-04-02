package com.github.marschall.spring4shell.boot.tomcat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;


@Import(PocWebMvcConfiguration.class)
@SpringBootConfiguration
@EnableAutoConfiguration
public class PocApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(PocApplication.class, args);
  }

}
