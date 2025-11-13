package com.gopay.app.interfaces;

import com.gopay.app.contracts.GenericResponse;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface JiraApiInterface {


    String CONTENT_TYPE_APPLICATION_JSON = "application/json";

    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST("/rest/api/3/issue")
    Call<GenericResponse<ResponseBody>> createIssue(
            @Header("Authorization") String basicAuth,
            @Body RequestBody body
    );


}
