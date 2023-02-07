package com.codelitt.technical.exercise.service.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;

public abstract class BaseService {

  @Value("${api.countries.url}")
  protected String baseUrl;

  /**
   * Defines the basic HttpHeaders.
   * @return the basic HttpHeaders
   */
  protected HttpHeaders getHttpHeaders() {
    final HttpHeaders headers = new HttpHeaders();
    headers.set("content-type", "application/json");
    return headers;
  }
}
