package com.gopay.app.clients.httpclients;

import com.gopay.app.contracts.*;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.Map;

public interface LarkClientInterface {
    @POST("open-apis/im/v1/messages/{message_id}/reply")
    Call<LarkResponse> sendReply(
            @Header("Authorization") String tenantToken,
            @Path("message_id") String messageId,
            @Body RequestBody body);

    @POST("auth/v3/tenant_access_token/internal")
    Call<TenantTokenResponse> requestTenantToken(@Body TenantTokenRequestBody body);
}
