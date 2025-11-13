package com.gopay.app.services;

import com.gopay.app.clients.httpclients.GitlabServiceAPIClient;
import com.gopay.app.contracts.GitlabResponse;
import com.gopay.app.models.GitlabCompareResponse;
import com.gopay.app.models.GitlabEnvironment;
import com.gopay.app.models.GitlabLastDeploymentInfoResponse;
import com.gopay.app.models.GitlabPipelineResponse;

import java.util.List;

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

        long environmentId = 11170; //hardcoded environment ID for AS
//        int serviceStart = deployPipelineLink.indexOf("gopay/") + "gopay/".length();
//        int serviceEnd = deployPipelineLink.indexOf("/-/pipelines/");
//        if (serviceStart == -1 || serviceEnd == -1 || serviceEnd <= serviceStart) {
//            throw new IllegalArgumentException("Invalid pipeline link format");
//        }
//        String serviceName = deployPipelineLink.substring(serviceStart, serviceEnd);
        String project = "gopay%2Fauthorization_service";// hardcoded for AS
        //String project = "gopay%2F" + serviceName;

//        List<GitlabEnvironment> environments = gitlabServiceAPIClient.getEnvironments(project);
//        long environmentId = environments.stream()
//                .filter(env -> "production".equalsIgnoreCase(env.getName()))
//                .findFirst()
//                .orElseThrow(() -> new IllegalStateException("Production environment not found"))
//                .getId();

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
        response.setCommitDiffs(compareResponse.getWebUrl());
        return response;
    }
}
