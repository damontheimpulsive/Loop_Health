package com.gopay.app.controllers;

import com.google.gson.Gson;
import com.gopay.app.services.JiraService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class PacmanController {


  private final Gson gson;
  private final JiraService jiraService;

  public Map<String, String> createPacman(Request req, Response res) throws IOException {


    log.info("Handling create pacman request {}");

   String pacmanTicketURL =  jiraService.executeJIRAIntegration();

   log.info("Pacman Ticket URL: {}", pacmanTicketURL);

    return Map.of("message", "pacmanTicketURL");
  }
}
