package com.gopay.app.services;

import com.gopay.app.clients.httpclients.GitlabServiceAPIClient;
import com.gopay.app.models.GitlabPipelineResponse;

public class GitlabService {
    private final GitlabServiceAPIClient gitlabServiceAPIClient;

    public GitlabService(GitlabServiceAPIClient gitlabServiceAPIClient) {
        this.gitlabServiceAPIClient = gitlabServiceAPIClient;
    }

    public GitlabPipelineResponse getGitlabPipeline(String project, long pipelineId) throws Exception {
        return gitlabServiceAPIClient.getPipeline(project, pipelineId);
    }
}
