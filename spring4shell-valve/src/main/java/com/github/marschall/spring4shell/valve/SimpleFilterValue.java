package com.github.marschall.spring4shell.valve;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.RemoteHostValve;
import org.apache.catalina.valves.ValveBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

public class SimpleFilterValue extends ValveBase {

  private static final Log LOG = LogFactory.getLog(RemoteHostValve.class);

  public SimpleFilterValue() {
    super(true);
  }

  @Override
  public void invoke(Request request, Response response) throws IOException, ServletException {
    if (isDenied(request)) {
      denyRequest(response);
    } else {
      getNext().invoke(request, response);
      
    }
  }

  private boolean isDenied(Request request) throws IOException {
    Enumeration<String> parameterNames = request.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String parameterName = (String) parameterNames.nextElement();
      if (isDenied(parameterName)) {
        LOG.warn("denied request with property: " + parameterName);
        return true;
      }
    }
    String method = request.getMethod();
    if (method.equals("POST")) {
      String contentType = request.getContentType();
      if (contentType.equalsIgnoreCase( "multipart/form-data")) {
        try {
          for (Part part : request.getParts()) {
            String partName = part.getName();
            if (isDenied(partName)) {
              LOG.warn("denied request with property: " + partName);
              return true;
            }
          }
        } catch (IllegalStateException | ServletException e) {
          LOG.warn("could not read form data", e);
        }
      }
    }
    return false;
  }

  private boolean isDenied(String parameterName) {
    return parameterName.contains("class.")
        || parameterName.contains("Class.")
        || parameterName.contains(".class")
        || parameterName.contains(".Class");
  }

  private void denyRequest(Response response) throws IOException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN);
  }

}
