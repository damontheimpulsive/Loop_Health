package com.gopay.app.clients.httpclients;


import com.gopay.app.models.GitlabPipelineResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface GitlabServiceInterface {
    @GET("/api/v4/projects/{project}/pipelines/{pipelineId}")
    Call<GitlabPipelineResponse> getPipeline(
            @Header("PRIVATE-TOKEN") String privateToken,
            @Path("project") String project,
            @Path("pipelineId") long pipelineId
    );
}
