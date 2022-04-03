package com.github.marschall.spring4shell.valve;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.http11.Http11NioProtocol;
import org.junit.jupiter.api.Test;

class SimpleFilterValueTests {
  
  @Test
  void invoke() throws LifecycleException, IOException {
    Tomcat tomcat = new Tomcat();

    Connector connector = new Connector(Http11NioProtocol.class.getName());
    connector.setPort(8080);
    connector.setProperty("address", "127.0.0.1");
    connector.setThrowOnFailure(true);
    tomcat.getService().addConnector(connector);
    tomcat.setConnector(connector);
    
    tomcat.getHost().getPipeline().addValve(new SimpleFilterValue());

    StandardContext context = (StandardContext) tomcat.addContext("", Paths.get("src", "test", "resources", "docroot").toAbsolutePath().toString());
    // these cause illegal access warnings
    context.setClearReferencesObjectStreamClassCaches(false);
    context.setClearReferencesThreadLocals(false);
    context.setClearReferencesRmiTargets(false);

    String servletName = "ok-servlet";
    Tomcat.addServlet(context, servletName, new OkServlet());
    context.addServletMappingDecoded("/", servletName);

    tomcat.start();

    try {
      sentNotBlockedRequest();
      sentBlockedRequest();
    } finally {
      tomcat.stop();
      tomcat.destroy();
    }
  }

  private void sentNotBlockedRequest() throws IOException {
    int statusCode = sendBody("module.resources.context.parent.pipeline.first.suffix=.j%25%7Bc2%7Di");
    assertEquals(HttpServletResponse.SC_OK, statusCode, "HTTP status code");
  }

  private void sentBlockedRequest() throws IOException {
    int statusCode = sendBody("class.module.classLoader.resources.context.parent.pipeline.first.suffix=.j%25%7Bc2%7Di");
    assertEquals(HttpServletResponse.SC_FORBIDDEN, statusCode, "HTTP status code");
  }

  private int sendBody(String body) throws IOException {
    URL url = new URL("http://127.0.0.1:8080/");
    byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    connection.setRequestProperty("Content-Length", Integer.toString(bytes.length));
    connection.setDoOutput(true);
    connection.connect();
    try {
      try (OutputStream outputStream = connection.getOutputStream()) {
        outputStream.write(bytes);
      }
      return connection.getResponseCode();
    } finally {
      connection.disconnect();
    }
  }

}
