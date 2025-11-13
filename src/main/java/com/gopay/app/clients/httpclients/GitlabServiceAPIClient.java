package com.gopay.app.clients.httpclients;

import com.gopay.app.models.GitlabCompareResponse;
import com.gopay.app.models.GitlabPipelineResponse;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GitlabServiceAPIClient {
    private final GitlabServiceInterface gitlabServiceInterface;
    private final String privateToken;
    private static final String BASE_URL = "https://source.golabs.io";

    public GitlabServiceAPIClient(String privateToken) {
        this.privateToken = privateToken;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.gitlabServiceInterface = retrofit.create(GitlabServiceInterface.class);
    }

    public GitlabPipelineResponse getPipeline(String project, long pipelineId) throws Exception {
        return gitlabServiceInterface.getPipeline(privateToken, project, pipelineId).execute().body();
    }
    public GitlabCompareResponse compareCommits(String project, String fromCommit, String toCommit) throws Exception {
        return gitlabServiceInterface.compareCommits(privateToken, project, fromCommit, toCommit).execute().body();
    }
}
