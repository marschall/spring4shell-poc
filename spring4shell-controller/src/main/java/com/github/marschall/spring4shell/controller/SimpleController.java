package com.github.marschall.spring4shell.controller;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
  
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @PostMapping(path =  "/model", consumes = MediaType.APPLICATION_JSON_VALUE)
  void jsonEnpoint(@RequestBody Model model) {
    LOG.info("jsonEnpoint");
  }

  @RequestMapping("/model")
  public void webDataBinderEndpoint(Model evalBean){
    LOG.info("webDataBinderEndpoint");
  }

}
