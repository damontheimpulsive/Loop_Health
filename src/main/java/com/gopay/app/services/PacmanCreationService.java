package com.gopay.app.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class PacmanCreationService {
    private final JiraService jiraService;

    public String createPacman(final String deploymentPipelineUrl) throws IOException {
        log.info("Handling create pacman request {}");
        String pacmanTicketURL =  jiraService.executeJIRAIntegration();
        log.info("Pacman Ticket URL: {}", pacmanTicketURL);
        return pacmanTicketURL;
    }
}
