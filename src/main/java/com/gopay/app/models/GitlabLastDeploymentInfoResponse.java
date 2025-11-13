package com.gopay.app.models;


import lombok.Data;

@Data
public class GitlabLastDeploymentInfoResponse {
    private String sha;
    private String webUrl;

}
