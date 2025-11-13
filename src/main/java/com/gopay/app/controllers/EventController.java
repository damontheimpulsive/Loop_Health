package com.gopay.app.controllers;

import com.google.gson.Gson;
import com.gopay.app.contracts.ChallengeRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class EventController {
  private final Gson gson;

  public Map<String, String> handle(Request req, Response res) {
    log.info("Handling request {}", req.body());
    final Map<String, Object> payload = gson.fromJson(req.body(), Map.class);

    final String eventType = (String) payload.get("type");
    if (eventType.equalsIgnoreCase("url_verification")) {
      log.info("Received URL verification event");
      final ChallengeRequest challengeRequest = gson.fromJson(req.body(), ChallengeRequest.class);
      return Map.of("challenge", challengeRequest.getChallenge());
    }
    log.info("Received unknown event type {}", eventType);
    return Map.of();
  }
}
