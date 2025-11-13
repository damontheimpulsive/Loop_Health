package com.gopay.app.controllers;

import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Map;

@Slf4j
public class HealthCheckController {
  public Map<String, String> checkReadiness(Request request, Response response) {
    return Map.of("message", "pong");
  }
}
