package com.gopay.app.controllers;

import com.google.gson.Gson;
import com.gopay.app.clients.httpclients.LarkClient;
import com.gopay.app.contracts.ChallengeRequest;
import com.gopay.app.contracts.LarkEventRequest;
import com.gopay.app.services.LarkService;
import com.gopay.app.services.PacmanCreationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class EventController {
  private final Gson gson;
  private final PacmanCreationService pacmanCreationService;
  private final LarkService larkService;

  public Map<String, String> handle(Request req, Response res) throws Exception {
    log.info("Handling request {}", req.body());
    final Map<String, Object> payload = gson.fromJson(req.body(), Map.class);

    final String eventType = (String) payload.get("type");
    if (eventType.equalsIgnoreCase("url_verification")) {
      log.info("Received URL verification event");
      final ChallengeRequest challengeRequest = gson.fromJson(req.body(), ChallengeRequest.class);
      return Map.of("challenge", challengeRequest.getChallenge());
    }

    log.info("Received unknown event type {}", eventType);
    final LarkEventRequest eventRequest = gson.fromJson(req.body(), LarkEventRequest.class);
    final String deploymentPipelineUrl = eventRequest.getEvent().getMessage().getContent();

    if (!isPipelineUrlValid(deploymentPipelineUrl)) {
      log.debug("Invalid deployment pipeline URL: {}", deploymentPipelineUrl);
      return Map.of("message", "Invalid deployment pipeline URL");
    }
    // handle errors
    final String pacmanUrl = pacmanCreationService.createPacman(deploymentPipelineUrl);
    log.info("Created Pacman at URL: {}", pacmanUrl);
    larkService.sendReply(eventRequest, "Pacman created successfully! Access it here: " + pacmanUrl);
    return Map.of("message", "ok");
  }

  public boolean isPipelineUrlValid(String url) {
    return url != null && url.startsWith("https://source.golabs.io/gopay");
  }
}
