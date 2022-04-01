package com.github.marschall.spring4shell.controller;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.multipart.support.MultipartFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class DemoWebApplicationInitializer implements WebApplicationInitializer {

  private static final String SERVLET_NAME = "poc-dispatcher";
  private static final int ONE_MB = 1024 * 1024 * 1024;

  @Override
  public void onStartup(ServletContext context) throws ServletException {

    AnnotationConfigWebApplicationContext dispatcherContext = registerApplicationContext(context);

    registerDispatcherServlet(context, dispatcherContext);
    registerFilter(context);
  }

  private void registerFilter(ServletContext context) {
    var dynamic = context.addFilter("multipart-filter", new MultipartFilter());
    dynamic.addMappingForServletNames(EnumSet.of(DispatcherType.REQUEST), false, SERVLET_NAME);
  }

  private void registerDispatcherServlet(ServletContext context,
      AnnotationConfigWebApplicationContext dispatcherContext) {
    var dynamic = context.addServlet(SERVLET_NAME, new DispatcherServlet(dispatcherContext));
    dynamic.addMapping("/poc/*");
    dynamic.setMultipartConfig(new MultipartConfigElement("", ONE_MB, ONE_MB, 0));
  }

  private AnnotationConfigWebApplicationContext registerApplicationContext(ServletContext context) {
    AnnotationConfigWebApplicationContext dispatcherContext = new AnnotationConfigWebApplicationContext();
    dispatcherContext.register(PocWebMvcConfiguration.class);
    // Manage the lifecycle of the root application context
    context.addListener(new ContextLoaderListener(dispatcherContext));
    return dispatcherContext;
  }

}
