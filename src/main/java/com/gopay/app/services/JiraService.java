package com.gopay.app.services;

import com.google.gson.Gson;
import com.gopay.app.contracts.GenericResponse;
import com.gopay.app.contracts.JiraResponse;
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

    public String executeJIRAIntegration() throws IOException {


        RequestBody body = RequestBody.create(MediaType.parse("application/json"), createRequestBody());

        Call<GenericResponse<ResponseBody>> call = jiraApiInterface.createIssue(
                "Basic ATATT3xFfGF0SnPmGHQz6odT5qc3e5NgJzijhAvFT4d9Coi0zBCPyiv1yMi3K1M1GAmZuQ_SnsM1o3RF_JilHgozdnSi3byUVw5mW7sjXTJNgVBw-K6pOcauDvuxRDX6xMNxq-al7weojjIdoGOph7KbF4C63G1eUYJqEwaHHpp_tyM6vsY7u4A=C06DB178",
                body
        );

        Response response = call.execute().raw();


        if (response.isSuccessful()) {
            log.info("JIRA response is successful", response.body().string());

            Gson gson = new Gson();
            JiraResponse jiraResponse = gson.fromJson(response.body().string(), JiraResponse.class);

            log.info("JIRA response after GSON deserialize", jiraResponse);

            String browseUrl = "https://go-jek.atlassian.net/browse/" + jiraResponse.getKey();

            return browseUrl;


        } else {
            log.error("Error responseCode : {} ",  response.code());
            assert response.body() != null;
            log.error("Error response body: {} ", response.body().string());
        }


        return null;
    }

    private String createRequestBody() {

        String json =

                "{\n" +
                        "  \"fields\": {\n" +
                        "    \"project\": { \"key\": \"PACMAN\" },\n" +
                        "    \"summary\": \"Authorization-service Production Deployment\",\n" +
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
                        "    \"issuetype\": { \"id\": \"14119\" },\n" +
                        "    \"customfield_14506\": { \"id\": \"15288\" },\n" +
                        "    \"customfield_14636\": { \"id\": \"16187\" },\n" +
                        "    \"customfield_14614\": [ { \"id\": \"15620\" } ],\n" +
                        "    \"customfield_14495\": \"https://source.golabs.io/gopay/authorization_service/-/compare/v7.11.0...v7.12.0?from_project_id=12649\",\n" +
                        "    \"customfield_14498\": {\n" +
                        "      \"type\": \"doc\",\n" +
                        "      \"version\": 1,\n" +
                        "      \"content\": [\n" +
                        "        { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"Changelog:\" } ] },\n" +
                        "        { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"- https://go-jek.atlassian.net/browse/ONLINE-3406\" } ] },\n" +
                        "        { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"- https://go-jek.atlassian.net/browse/ONLINE-3321\" } ] },\n" +
                        "        { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"- Notes: deploy during off-peak hours\" } ] }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    \"customfield_14499\": [\n" +
                        "      { \"accountId\": \"712020:1daa816d-bbfd-440c-95b6-c9cbf0e7578d\" }\n" +
                        "    ],\n" +
                        "    \"customfield_14455\": [\n" +
                        "      { \"accountId\": \"5ff407614d2179006eab9b52\" }\n" +
                        "    ],\n" +
                        "    \"customfield_14755\": { \"id\": \"16342\" },\n" +
                        "    \"customfield_14635\": { \"id\": \"16157\" },\n" +
                        "    \"customfield_14797\": \"2025-11-14\",\n" +
                        "    \"customfield_14501\": { \"id\": \"15278\" }\n" +
                        "  }\n" +
                        "}";

        return json;
    }

}
