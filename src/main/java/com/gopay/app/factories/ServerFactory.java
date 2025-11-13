package com.gopay.app.factories;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gopay.app.Server;
import com.gopay.app.clients.httpclients.LarkClient;
import com.gopay.app.controllers.EventController;
import com.gopay.app.controllers.GitlabController;
import com.gopay.app.controllers.HealthCheckController;
import com.gopay.app.services.LarkService;
import com.gopay.app.services.PacmanCreationService;
import com.gopay.app.services.GitlabService;
import com.gopay.app.clients.httpclients.GitlabServiceAPIClient;
import com.gojek.ApplicationConfiguration;
import com.gojek.Figaro;

public class ServerFactory {
    public static Server createAPIServer() {
        final ApplicationConfiguration configuration = Figaro.configure(null);
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        final String gitlabToken = configuration.getValueAsString("GITLAB_PRIVATE_TOKEN");
        final GitlabServiceAPIClient gitlabServiceAPIClient = new GitlabServiceAPIClient(gitlabToken);
        final GitlabService gitlabService = new GitlabService(gitlabServiceAPIClient);
        final GitlabController gitlabController = new GitlabController(gitlabService);

        return Server.builder()
                .healthCheckController(new HealthCheckController())
                .eventController(new EventController(gson, new PacmanCreationService(), new LarkService(new LarkClient())))
                .gitlabController(gitlabController)
                .gson(gson)
                .build();
    }
}