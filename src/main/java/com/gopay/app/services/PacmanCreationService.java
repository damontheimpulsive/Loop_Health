package com.gopay.app.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class PacmanCreationService {

    private final GitlabService gitlabService;
    private final JiraService jiraService;

    public String createPacman(final String deploymentPipelineUrl) throws Exception {
        log.info("Handling create pacman request {}");


        // gitlabService.getCombinedGitlabInfo(1L);

        String pacmanTicketURL = jiraService.executeJIRAIntegration();
        log.info("Pacman Ticket URL: {}", pacmanTicketURL);
        return pacmanTicketURL;
    }
}
