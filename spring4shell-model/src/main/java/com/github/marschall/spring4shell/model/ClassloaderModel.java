package com.github.marschall.spring4shell.model;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.TreeSet;

public class ClassloaderModel {

  private static final Set<String> IGNORED_PROPERTIES = Set.of(
      // normal getters
      "class", "name", "parent",
      // additional getters on ClassLoader
      "defaultAssertionStatus", "definedPackages", "platformClassLoader", "registeredAsParallelCapable", "systemClassLoader", "unnamedModule");
  
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

  public static ClassloaderModel createFrom(ClassLoader classLoader) {
    ClassLoader parent = classLoader.getParent();
    ClassloaderModel parentModel = parent != null ? createFrom(parent) : null;

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

}
