package com.gopay.app.controllers;


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
        String project = req.queryParams("gopay%2Fauthorization_service");
        long pipelineId = Long.parseLong(req.queryParams("pipelineId"));
        try {
            GitlabPipelineResponse response = gitlabService.getGitlabPipeline(project, pipelineId);
            return response;
        } catch (Exception e) {
            res.status(500);
            return Collections.singletonMap("error", e.getMessage());
        }
    }
}
