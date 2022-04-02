package com.github.marschall.spring4shell.boot.jetty;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.marschall.spring4shell.model.ClassloaderModel;

@RestController
public class PocController {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @PostMapping(path =  "/poc/model", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ClassloaderModel jsonEnpoint(@RequestBody Model model) {
    LOG.info("jsonEnpoint");

    ClassLoader classLoader = this.getClass().getClassLoader();
    return ClassloaderModel.createFrom(classLoader);
  }

  @RequestMapping("/poc/model")
  public void webDataBinderEndpoint(Model model){
    LOG.info("webDataBinderEndpoint");
  }

}
