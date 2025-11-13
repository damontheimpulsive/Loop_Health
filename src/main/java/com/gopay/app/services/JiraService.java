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

        String apmLink = "https://katulampa.golabs.io/d/besyeicntab5sd/service?orgId=1&var-cluster_name=al-gp-id-p-01&var-datasource=000000001&var-env=production&var-service=authorization-service-app-gsh&var-interval=1m&from=now-3h&to=now&timezone=browser&var-newrelic_datasource=feuxnv85t73swe&var-outbound_service=$__all&var-pg_app_name=$__all&var-service_shortname=authorization-service&var-mongo_app_name=$__all&var-redis_app_name=$__all&var-rabbitmq_app_name=$__all&var-rabbitmq_cluster_name=$__all&var-apdex_lower_latency=250&var-apdex_upper_latency=1000&var-namespace=payments-experience&var-barito_app_group_name=guto&var-bad_http_status_code=4..%7C5..&var-newrelic_guid=MjA3MTA4NHxBUE18QVBQTElDQVRJT058NTIzMjA5Njg4&var-newrelic_account_id=2071084&var-newrelic_transactions=$__all&var-pods=$__all&refresh=5m";

        String katulampaLink = "https://katulampa.golabs.io/d/Iicq0uc7k/authorization-service?var-interval=$__auto&orgId=1&from=now-24h&to=now&timezone=Asia%2FKolkata&var-env=production&var-merchant_name=$__all&var-error_code=$__all&refresh=5m";

        String baritoLink = "https://barito-viewer.golabs.io/togu/goto/86e31150-c09a-11f0-acd1-ed09208ebeb6";

        String compareLink = "https://source.golabs.io/gopay/authorization_service/-/compare/v7.11.0...v7.12.0?from_project_id=12649";

        String pipeline1 = "https://source.golabs.io/gopay/authorization_service/-/pipelines/12936112";
        String pipeline2 = "https://source.golabs.io/gopay/authorization_service/-/pipelines/12896592";

        String diffLink = "https://yggdrasil.teleport-proxy.apps.gtflabs.io/ui/v2/authorization_service/diff?first_tag=production&first_version=0.667.6&second_tag=production&second_version=0.667.13";

        String json =

                "{\n" +
                        "  \"fields\": {\n" +
                        "    \"project\": { \"key\": \"PACMAN\" },\n" +
                        "    \"summary\": \"Authorization Service Prod Deployment\",\n" +
                        "    \"description\": {\n" +
                        "      \"type\": \"doc\",\n" +
                        "      \"version\": 1,\n" +
                        "      \"content\": [\n" +
                        "        {\n" +
                        "          \"type\": \"paragraph\",\n" +
                        "          \"content\": [ { \"type\": \"text\", \"text\": \"Service Monitoring Links\" } ]\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"type\": \"bulletList\",\n" +
                        "          \"content\": [\n" +
                        "            {\n" +
                        "              \"type\": \"listItem\",\n" +
                        "              \"content\": [ {\n" +
                        "                \"type\": \"paragraph\",\n" +
                        "                \"content\": [ {\n" +
                        "                  \"type\": \"text\",\n" +
                        "                  \"text\": \"APM\",\n" +
                        "                  \"marks\": [ {\n" +
                        "                    \"type\": \"link\",\n" +
                        "                    \"attrs\": { \"href\": \"" + apmLink + "\" }\n" +
                        "                  } ]\n" +
                        "                } ]\n" +
                        "              } ]\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"type\": \"listItem\",\n" +
                        "              \"content\": [ {\n" +
                        "                \"type\": \"paragraph\",\n" +
                        "                \"content\": [ {\n" +
                        "                  \"type\": \"text\",\n" +
                        "                  \"text\": \"Katulampa\",\n" +
                        "                  \"marks\": [ {\n" +
                        "                    \"type\": \"link\",\n" +
                        "                    \"attrs\": { \"href\": \"" + katulampaLink + "\" }\n" +
                        "                  } ]\n" +
                        "                } ]\n" +
                        "              } ]\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"type\": \"listItem\",\n" +
                        "              \"content\": [ {\n" +
                        "                \"type\": \"paragraph\",\n" +
                        "                \"content\": [ {\n" +
                        "                  \"type\": \"text\",\n" +
                        "                  \"text\": \"Barito\",\n" +
                        "                  \"marks\": [ {\n" +
                        "                    \"type\": \"link\",\n" +
                        "                    \"attrs\": { \"href\": \"" + baritoLink + "\" }\n" +
                        "                  } ]\n" +
                        "                } ]\n" +
                        "              } ]\n" +
                        "            }\n" +
                        "          ]\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    \"issuetype\": { \"id\": \"14119\" },\n" +
                        "    \"customfield_14506\": { \"id\": \"15288\" },\n" +
                        "    \"customfield_14636\": { \"id\": \"16187\" },\n" +
                        "    \"customfield_14614\": [ { \"id\": \"15620\" } ],\n" +
                        "    \"customfield_14495\": \"" + compareLink + "\",\n" +
                        "    \"customfield_14498\": {\n" +
                        "      \"type\": \"doc\",\n" +
                        "      \"version\": 1,\n" +
                        "      \"content\": [\n" +
                        "        { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"https://go-jek.atlassian.net/browse/ONLINE-3406\" } ] },\n" +
                        "        { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"https://go-jek.atlassian.net/browse/ONLINE-3321\" } ] },\n" +
                        "        { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"- Notes: deploy during off-peak hours\" } ] }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    \"customfield_14499\": [ { \"accountId\": \"712020:1daa816d-bbfd-440c-95b6-c9cbf0e7578d\" } ],\n" +
                        "    \"customfield_14455\": [ { \"accountId\": \"5ff407614d2179006eab9b52\" } ],\n" +
                        "    \"customfield_14755\": { \"id\": \"16342\" },\n" +
                        "    \"customfield_14635\": { \"id\": \"16157\" },\n" +
                        "    \"customfield_14797\": \"2025-11-13\",\n" +
                        "    \"customfield_14501\": { \"id\": \"15278\" },\n" +
                        "    \"customfield_14487\": \"" + pipeline1 + "\",\n" +
                        "    \"customfield_14488\": \"" + pipeline2 + "\",\n" +
                        "    \"customfield_14518\": {\n" +
                        "      \"type\": \"doc\",\n" +
                        "      \"version\": 1,\n" +
                        "      \"content\": [ {\n" +
                        "        \"type\": \"paragraph\",\n" +
                        "        \"content\": [ { \"type\": \"text\", \"text\": \"" + diffLink + "\" } ]\n" +
                        "      } ]\n" +
                        "    },\n" +
                        "    \"customfield_14500\": {\n" +
                        "      \"type\": \"doc\",\n" +
                        "      \"version\": 1,\n" +
                        "      \"content\": [ { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"Deploy Canary Pod -->  Monitor Canary Pods --> If everything looks good then deploy stable pods\" } ] } ]\n" +
                        "    },\n" +
                        "    \"customfield_14645\": {\n" +
                        "      \"type\": \"doc\",\n" +
                        "      \"version\": 1,\n" +
                        "      \"content\": [ { \"type\": \"paragraph\", \"content\": [ { \"type\": \"text\", \"text\": \"Trigger Rollback Pipeline\" } ] } ]\n" +
                        "    }\n" +
                        "  }\n" +
                        "}";

        return json;
    }

}
