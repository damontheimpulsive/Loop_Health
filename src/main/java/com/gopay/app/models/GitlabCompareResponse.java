package com.gopay.app.models;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class GitlabCompareResponse {
    @SerializedName("web_url")
    private String webUrl;
}
