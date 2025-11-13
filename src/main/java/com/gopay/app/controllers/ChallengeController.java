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
public class ChallengeController {
  private final Gson gson;

  public Map<String, String> verifyChallenge(Request req, Response res) {
    log.info("Handling verify challenge request {}", req.body());
    final ChallengeRequest challengeRequest = gson.fromJson(req.body(), ChallengeRequest.class);
    return Map.of("challenge", challengeRequest.getChallenge());
  }
}
