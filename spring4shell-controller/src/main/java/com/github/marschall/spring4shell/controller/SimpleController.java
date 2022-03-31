package com.github.marschall.spring4shell.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
public class SimpleController {
  
  @PostMapping("/employees")
  void simplePost(@RequestBody Model model) {
    // empty
  }

}
