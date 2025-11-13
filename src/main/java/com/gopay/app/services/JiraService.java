package com.gopay.app.services;

import com.google.gson.Gson;
import com.gopay.app.contracts.GenericResponse;
import com.gopay.app.interfaces.JiraApiInterface;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;

import java.io.IOException;

@Slf4j
@AllArgsConstructor
public class JiraService {


    private final JiraApiInterface jiraApiInterface;
    private final Gson gson;


    public void executeJIRAIntegration() throws IOException {


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), createRequestBody());

        Call<GenericResponse<ResponseBody>> call = jiraApiInterface.createIssue(
                "Basic ATATT3xFfGF0SnPmGHQz6odT5qc3e5NgJzijhAvFT4d9Coi0zBCPyiv1yMi3K1M1GAmZuQ_SnsM1o3RF_JilHgozdnSi3byUVw5mW7sjXTJNgVBw-K6pOcauDvuxRDX6xMNxq-al7weojjIdoGOph7KbF4C63G1eUYJqEwaHHpp_tyM6vsY7u4A=C06DB178",
                body
        );

        Response response = call.execute().raw();

        if (response.isSuccessful()) {
            log.info("JIRA response is successful", response.body().string());
        } else {
            log.error("Error: " + response.message());
        }

    }

    private String createRequestBody() {

        String json =
                "{\n" +
                        "  \"fields\": {\n" +
                        "    \"project\": { \"key\": \"PACMAN\" },\n" +
                        "    \"summary\": \"merchant-adapter deployment\",\n" +
                        "    \"description\": {\n" +
                        "      \"type\": \"doc\",\n" +
                        "      \"version\": 1,\n" +
                        "      \"content\": [\n" +
                        "        {\n" +
                        "          \"type\": \"paragraph\",\n" +
                        "          \"content\": [\n" +
                        "            { \"type\": \"text\", \"text\": \"Created by pacman bot\" }\n" +
                        "          ]\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    \"issuetype\": { \"id\": \"14119\" }\n" +
                        "  }\n" +
                        "}";

        return json;
    }


}
