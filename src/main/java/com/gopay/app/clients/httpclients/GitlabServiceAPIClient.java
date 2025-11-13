package com.gopay.app.clients.httpclients;

import com.gopay.app.models.GitlabPipelineResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitlabServiceAPIClient {
    private final GitlabServiceInterface gitlabServiceInterface;
    private static final String BASE_URL = "https://source.golabs.io";
    private static final String PRIVATE_TOKEN = "ACCESS_TOKEN";

    public GitlabServiceAPIClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.gitlabServiceInterface = retrofit.create(GitlabServiceInterface.class);
    }

    public GitlabPipelineResponse getPipeline(String project, long pipelineId) throws Exception {
        return gitlabServiceInterface.getPipeline(PRIVATE_TOKEN, project, pipelineId).execute().body();
    }
}
