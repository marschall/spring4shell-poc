package com.github.marschall.spring4shell.controller;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {
  
  private static final Set<String> IGNORED_PROPERTIES = Set.of("class", "name", "parent");
  
  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @PostMapping(path =  "/model", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ClassloaderModel jsonEnpoint(@RequestBody Model model) {
    LOG.info("jsonEnpoint");
    ClassLoader classLoader = this.getClass().getClassLoader();
    
    return buildClassloaderModel(classLoader);
  }
  
  private static ClassloaderModel buildClassloaderModel(ClassLoader classLoader) {
    ClassLoader parent = classLoader.getParent();
    ClassloaderModel parentModel = parent != null ? buildClassloaderModel(parent) : null;

    String className = classLoader.getClass().getName();
    ClassloaderModel model = new ClassloaderModel(className, parentModel);
    for (Method method : classLoader.getClass().getMethods()) {
      if (isProperty(method)) {
        String propertyName = getPropertyName(method);
        if (!IGNORED_PROPERTIES.contains(propertyName)) {
          model.addProperty(propertyName);
        }
      }
    }
    
    return model;
  }

  private static String getPropertyName(Method method) {
    String methodName = method.getName();
    if (methodName.startsWith("get") || methodName.startsWith("set")) {
      return Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
    } else if (methodName.startsWith("is")) {
      return Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
    }
    throw new IllegalArgumentException("not a property: " + methodName);
  }

  private static boolean isProperty(Method method) {
    String methodName = method.getName();
    if (methodName.startsWith("get")) {
      return methodName.length() > 3
          && method.getParameterCount() == 0
          && Character.isUpperCase(methodName.charAt(3))
          && method.getReturnType() != Void.TYPE;
    } else if (methodName.startsWith("is")) {
      return methodName.length() > 2
          && method.getParameterCount() == 0
          && Character.isUpperCase(methodName.charAt(2))
          && method.getReturnType() == Boolean.TYPE;
    } else if (methodName.startsWith("set")) {
      return methodName.length() > 3
          && method.getParameterCount() == 1
          && Character.isUpperCase(methodName.charAt(3))
          && method.getReturnType() == Void.TYPE;
    }
    return false;
  }

  @RequestMapping("/model")
  public void webDataBinderEndpoint(Model model){
    LOG.info("webDataBinderEndpoint");
  }

}
