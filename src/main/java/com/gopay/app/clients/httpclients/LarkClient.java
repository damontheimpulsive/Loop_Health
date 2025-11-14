package com.gopay.app.clients.httpclients;

import com.gojek.ApplicationConfiguration;
import com.google.gson.Gson;
import com.gopay.app.contracts.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Slf4j
public class LarkClient {
    private final LarkClientInterface larkClientInterface;
    private final Gson gson;
    private static final String BASE_URL = "https://open.larksuite.com/open-apis/im/";
    private final ApplicationConfiguration config;

    public LarkClient(Gson gson, ApplicationConfiguration config) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        this.larkClientInterface = retrofit.create(LarkClientInterface.class);
        this.gson = gson;
        this.config = config;
    }

    public void sendReply(String messageId, String body) throws Exception {

        log.info("Sending reply for MessageId : {} ", messageId);
        final String TENANT_ACCESS_TOKEN = config.getValueAsString("LARK_TENANT_ACCESS_TOKEN", "");

        Request request = new Request.Builder()
                .url(String.format("https://open.larksuite.com/open-apis/im/v1/messages/%s/reply", messageId))
                .method("POST",createRequestBody(body))
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + TENANT_ACCESS_TOKEN)
                .build();
        Response larkResponse = new OkHttpClient().newBuilder().build().newCall(request).execute();

        LarkResponse larkResponse1 = gson.fromJson(larkResponse.body().string(), LarkResponse.class);

        log.info("Lark reply sent responseCode : {} ", larkResponse.code());
        if (!larkResponse.isSuccessful()) {
            /*log.error("Lark reply failed with code 99991663: Invalid tenant access token");
            log.info("Requesting new tenant access token");
            Call<TenantTokenResponse> tokenCall = larkClientInterface.requestTenantToken(
                    new TenantTokenRequestBody("cli_a99fc0eb2e39ded0", "iJEr5mZZwn13anjBU2UQBdLUxfRmLHB0")
            );
            retrofit2.Response<TenantTokenResponse> tokenResponse = tokenCall.execute();
            if (!tokenResponse.isSuccessful()) {
                log.error("TenantToken Error responseCode : {} ", larkResponse.code());
                return;
            }

            TenantTokenResponse tenantTokenResponse = tokenResponse.body();
            String newTenantToken = tenantTokenResponse.getTenantAccessToken();
            log.info("Obtained new tenant access token");

            // Retry sending the reply with the new token
            Call<LarkResponse> replyCall = this.larkClientInterface.sendReply(
                    "Bearer "+newTenantToken,
                    messageId,
                    createRequestBody(body)
            );
            retrofit2.Response<LarkResponse> retryResponse = replyCall.execute();*/
            log.error("Lark reply sending failed: {}", larkResponse1.getCode());
            return;
        }
        log.info("Lark reply sent successfully: {}", larkResponse.body());
    }

    private RequestBody createRequestBody(String content) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json"),
                String.format("{\n\t\"content\": \"{\\\"text\\\":\\\"%s\\\"}\",\n\t\"msg_type\": \"text\"\n}", content));
        return body;
    }
}