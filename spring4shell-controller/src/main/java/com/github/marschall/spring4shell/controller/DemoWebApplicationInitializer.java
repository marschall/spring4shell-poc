package com.github.marschall.spring4shell.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class DemoWebApplicationInitializer implements WebApplicationInitializer {

  @Override
  public void onStartup(ServletContext context) throws ServletException {
    AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
    
    dispatcherContext.register(PocWebMvcConfiguration.class);

    Dynamic dispatcher = context.addServlet("poc-dispatcher", new DispatcherServlet(dispatcherContext));
    dispatcher.addMapping("/poc/*");
  }

}
