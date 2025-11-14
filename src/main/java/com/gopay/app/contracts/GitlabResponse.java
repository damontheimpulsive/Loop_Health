package com.gopay.app.contracts;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class GitlabResponse {

    String deployPipelineLink;
    String rollbackPipelineLink;
    String commitDiffs;
}
