package com.gopay.app;

import com.google.gson.Gson;
import com.gopay.app.controllers.EventController;
import com.gopay.app.controllers.GitlabController;
import com.gopay.app.controllers.HealthCheckController;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

import static spark.Spark.*;

@Builder
@Slf4j
public class Server {

    private final HealthCheckController healthCheckController;
    private final EventController eventController;
    private final GitlabController gitlabController;
    private final Gson gson;

    public void start() {

        log.info("Starting Spark REST API server......");
        port(8080);
        setupEndpoints();
        before("/*", (request, response) -> response.type("application/json;charset=utf-8"));
    }

    public void stop() {
        log.info("Stopping Spark REST API server...");
        Spark.stop();
    }

    private void setupEndpoints() {
        // Health check endpoint
        path("",
                () -> {
                    get("/healthz", healthCheckController::checkReadiness, gson::toJson);
                }
        );

        // Application specific APIs
        post("/events", eventController::handle, gson::toJson);
        get("/gitlab/pipeline", gitlabController::getGitlabPipeline, gson::toJson);
        get("/gitlab/compare", gitlabController::compareCommits, gson::toJson);
        get("/gitlab/combined-info", gitlabController::getCombinedGitlabInfo, gson::toJson);
    }
}
