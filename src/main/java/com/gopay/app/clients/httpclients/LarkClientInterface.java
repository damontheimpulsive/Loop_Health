package com.gopay.app.clients.httpclients;

import com.gopay.app.contracts.ReplyContract;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LarkClientInterface {
    @POST("open-apis/im/v1/messages/{message_id}/reply")
    Call<Void> sendReply(
            @Header("Authorization") String tenantToken,
            @Path("message_id") String messageId,
            @Body ReplyContract text);
}
