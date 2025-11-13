package com.gopay.app;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.gopay.app.controllers.HealthCheckController;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import spark.Spark;

@Builder
@Slf4j
public class Server {
    private final HealthCheckController healthCheckController;
    private final Gson gson;

    public void start() {
        log.info("Starting Spark REST API server......");
        port(8000);
        setupEndpoints();
        before("/*", (request, response) -> response.type("application/json;charset=utf-8"));
    }

    public void stop() {
        log.info("Stopping Spark REST API server...");
        Spark.stop();
    }

    private void setupEndpoints() {
        // Health check endpoint
        path(
                "",
                () -> {
                    get("/healthz", healthCheckController::checkReadiness, gson::toJson);
                });

        // Application specific APIs
        final String API_VERSION = "/api/v1";
    }
}
