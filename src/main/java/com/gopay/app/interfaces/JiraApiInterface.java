package com.gopay.app.interfaces;

import com.gopay.app.contracts.JiraResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface JiraApiInterface {


    String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST("/rest/api/3/issue")
    Call<JiraResponse> createIssue(
            @Header("Authorization") String basicAuth,
            @Body RequestBody body
    );


}
