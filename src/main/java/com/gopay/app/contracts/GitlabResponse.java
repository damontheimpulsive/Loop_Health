package com.gopay.app.contracts;

import lombok.Data;

@Data
public class GitlabResponse {

    String deployPipelineLink;
    String rollbackPipelineLink;
    String commitDiffs;
}
