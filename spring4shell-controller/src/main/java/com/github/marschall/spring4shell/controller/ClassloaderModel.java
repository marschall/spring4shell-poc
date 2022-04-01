package com.github.marschall.spring4shell.controller;

import java.util.Set;
import java.util.TreeSet;

public class ClassloaderModel {
  
  private final String className;
  
  private final ClassloaderModel parent;
  
  private final Set<String> properties;

  public ClassloaderModel(String className, ClassloaderModel parent) {
    this.className = className;
    this.parent = parent;
    this.properties = new TreeSet<>();
  }
  
  public ClassloaderModel(String className) {
    this(className, null);
  }

  public Set<String> getProperties() {
    return properties;
  }

  public void addProperty(String property) {
    this.properties.add(property);
  }

  public String getClassName() {
    return className;
  }

  public ClassloaderModel getParent() {
    return parent;
  }

}
