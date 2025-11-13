package com.gopay.app.clients.httpclients;

import com.gopay.app.contracts.ReplyContract;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LarkClient {
    private final LarkClientInterface larkClientInterface;
    private static final String BASE_URL = "https://open.larksuite.com/open-apis/im";

    public LarkClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.larkClientInterface = retrofit.create(LarkClientInterface.class);
    }

    public boolean sendReply(String messageId, String token, String body) throws Exception {
        return this.larkClientInterface.sendReply(
                "Bearer " + token,
                messageId,
                new ReplyContract("text", body)
        ).execute().isSuccessful();
    }
}
