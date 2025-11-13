package com.gopay.app.controllers;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class PacmanController {
  private final Gson gson;

  public Map<String, String> createPacman(Request req, Response res) {
    log.info("Handling create pacman request {}");
    return Map.of("message", "dummy response");
  }
}
