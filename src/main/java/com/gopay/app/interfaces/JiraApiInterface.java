package com.gopay.app.interfaces;

import com.gopay.app.contracts.GenericResponse;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.nio.charset.StandardCharsets;
import java.util.Base64;


public interface JiraApiInterface {


    String CONTENT_TYPE_APPLICATION_JSON = "Content-Type: application/json";
    String apiToken = "ATATT3xFfGF0SnPmGHQz6odT5qc3e5NgJzijhAvFT4d9Coi0zBCPyiv1yMi3K1M1GAmZuQ_SnsM1o3RF_" +
            "JilHgozdnSi3byUVw5mW7sjXTJNgVBw-K6pOcauDvuxRDX6xMNxq-al7weojjIdoGOph7KbF4C63G1eUYJqEwaHHpp_tyM6vsY7u4A=C06DB178";
    String auth = "sumit.kandoi@gojek.com" + ":" + apiToken;
    String basicAuth = Base64.getEncoder()
            .encodeToString(auth.getBytes(StandardCharsets.UTF_8));


    @Headers({CONTENT_TYPE_APPLICATION_JSON})
    @POST("/rest/api/3/issue")
    Call<GenericResponse<ResponseBody>> createIssue(
            @Header("Authorization") String basicAuth,
            @Body RequestBody body
    );


}
