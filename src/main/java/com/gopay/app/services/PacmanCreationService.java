package com.gopay.app.services;

import com.gopay.app.contracts.GitlabResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class PacmanCreationService {

    private final GitlabService gitlabService;
    private final JiraService jiraService;

    public String createPacman(final String deploymentPipelineUrl) throws Exception {


        log.info("Handling create pacmanRequest with deploymentPipelineUrl: {}", deploymentPipelineUrl);


        // gitlabService.getCombinedGitlabInfo(1L);

        GitlabResponse gitlabResponse =  GitlabResponse.
                builder()
                .commitDiffs("")
                .deployPipelineLink(deploymentPipelineUrl)
                .rollbackPipelineLink("").build();

        String pacmanTicketURL = jiraService.executeJIRAIntegration(gitlabResponse);

        log.info("Pacman Ticket URL: {}", pacmanTicketURL);
        return pacmanTicketURL;
    }
}
