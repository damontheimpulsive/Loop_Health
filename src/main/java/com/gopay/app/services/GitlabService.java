package com.gopay.app.services;

import com.gopay.app.clients.httpclients.GitlabServiceAPIClient;
import com.gopay.app.contracts.GitlabResponse;
import com.gopay.app.models.GitlabCompareResponse;
import com.gopay.app.models.GitlabLastDeploymentInfoResponse;
import com.gopay.app.models.GitlabPipelineResponse;

public class GitlabService {
    private final GitlabServiceAPIClient gitlabServiceAPIClient;

    public GitlabService(GitlabServiceAPIClient gitlabServiceAPIClient) {
        this.gitlabServiceAPIClient = gitlabServiceAPIClient;
    }

    public GitlabPipelineResponse getGitlabPipeline(String project, long pipelineId) throws Exception {
        return gitlabServiceAPIClient.getPipeline(project, pipelineId);
    }
    public GitlabCompareResponse compareCommits(String project, String fromCommit, String toCommit) throws Exception {
        return gitlabServiceAPIClient.compareCommits(project, fromCommit, toCommit);
    }
    public GitlabLastDeploymentInfoResponse getLastDeploymentWebUrlAndSha(String project, long environmentId) throws Exception {
        return gitlabServiceAPIClient.getEnvironment(project, environmentId);
    }

    public GitlabResponse getCombinedGitlabInfo(String deployPipelineLink) throws Exception {
        String project = "gopay%2Fauthorization_service";
        long environmentId = 11170;

        int idx = deployPipelineLink.lastIndexOf("/pipelines/");
        if (idx == -1) {
            throw new IllegalArgumentException("Invalid pipeline link format");
        }
        String idStr = deployPipelineLink.substring(idx + "/pipelines/".length());
        long pipelineId = Long.parseLong(idStr);

        GitlabPipelineResponse pipelineResponse = getGitlabPipeline(project, pipelineId);
        GitlabLastDeploymentInfoResponse lastDeployment = getLastDeploymentWebUrlAndSha(project, environmentId);
        GitlabCompareResponse compareResponse = compareCommits(
                project,
                lastDeployment.getSha(),
                pipelineResponse.getSha()
        );

        GitlabResponse response = new GitlabResponse();
        response.setDeployPipelineLink(pipelineResponse.getWebUrl());
        response.setRollbackPipelineLink(lastDeployment.getWebUrl());
        response.setCommitDiffs(compareResponse.getWeb_url());
        return response;
    }
}
