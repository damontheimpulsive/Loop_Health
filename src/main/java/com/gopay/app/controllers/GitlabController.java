package com.gopay.app.controllers;

import com.gopay.app.contracts.GitlabResponse;
import com.gopay.app.models.GitlabCompareResponse;
import com.gopay.app.services.GitlabService;
import com.gopay.app.models.GitlabPipelineResponse;
import spark.Request;
import spark.Response;
import java.util.Collections;

public class GitlabController {
    private final GitlabService gitlabService;

    public GitlabController(GitlabService gitlabService) {
        this.gitlabService = gitlabService;
    }

    public Object getGitlabPipeline(Request req, Response res) {
        String project = req.queryParams("project");
        long pipelineId = Long.parseLong(req.queryParams("pipelineId"));
        try {
            GitlabPipelineResponse response = gitlabService.getGitlabPipeline(project, pipelineId);
            return response;
        } catch (Exception e) {
            res.status(500);
            return Collections.singletonMap("error", e.getMessage());
        }
    }
    public Object compareCommits(Request req, Response res) {
        String project = req.queryParams("project");
        String fromCommit = req.queryParams("from");
        String toCommit = req.queryParams("to");
        try {
            GitlabCompareResponse response = gitlabService.compareCommits(project, fromCommit, toCommit);
            return response;
        } catch (Exception e) {
            res.status(500);
            return Collections.singletonMap("error", e.getMessage());
        }
    }
    public Object getCombinedGitlabInfo(Request req, Response res) {
        String deployPipelineLink = req.queryParams("deployPipelineLink");
        try {
            GitlabResponse response = gitlabService.getCombinedGitlabInfo(deployPipelineLink);
            return response;
        } catch (Exception e) {
            res.status(500);
            return Collections.singletonMap("error", e.getMessage());
        }
    }
}