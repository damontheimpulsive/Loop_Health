package com.gopay.app.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GitlabEnvironmentResponse {
    @SerializedName("last_deployment")
    private LastDeployment lastDeployment;

    @Data
    public static class LastDeployment {
        private String sha;
        private Deployable deployable;

        @Data
        public static class Deployable {
            private Pipeline pipeline;

            @Data
            public static class Pipeline {
                @SerializedName("web_url")
                private String webUrl;
            }
        }
    }
}