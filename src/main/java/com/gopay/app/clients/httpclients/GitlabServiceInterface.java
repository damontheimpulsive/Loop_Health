package com.gopay.app.clients.httpclients;


import com.gopay.app.models.GitlabCompareResponse;
import com.gopay.app.models.GitlabEnvironment;
import com.gopay.app.models.GitlabLastDeploymentInfoResponse;
import com.gopay.app.models.GitlabPipelineResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface GitlabServiceInterface {
    @GET("/api/v4/projects/{project}/pipelines/{pipelineId}")
    Call<GitlabPipelineResponse> getPipeline(
            @Header("PRIVATE-TOKEN") String privateToken,
            @Path("project") String project,
            @Path("pipelineId") long pipelineId
    );

    @GET("/api/v4/projects/{project}/repository/compare")
    Call<GitlabCompareResponse> compareCommits(
            @Header("PRIVATE-TOKEN") String privateToken,
            @Path("project") String project,
            @Query("from") String fromCommit,
            @Query("to") String toCommit
    );
    @GET("/api/v4/projects/{project}/environments/{environmentId}")
    Call<GitlabLastDeploymentInfoResponse> getLastDeployment(
            @Header("PRIVATE-TOKEN") String privateToken,
            @Path("project") String project,
            @Path("environmentId") long environmentId
    );
    @GET("/api/v4/projects/{project}/environments")
    Call<List<GitlabEnvironment>> getEnvironments(
            @Header("PRIVATE-TOKEN") String privateToken,
            @Path("project") String project
    );
}
