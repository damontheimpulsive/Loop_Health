package com.gopay.app.factories;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gopay.app.Server;
import com.gopay.app.controllers.ChallengeController;
import com.gopay.app.controllers.PacmanController;

public class ServerFactory {
    public static Server createAPIServer() {
        final Gson gson = new GsonBuilder()
                        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                        .create();
        return Server.builder()
                .challengeController(new ChallengeController(gson))
                .pacmanController(new PacmanController(gson))
                .build();
    }
}
