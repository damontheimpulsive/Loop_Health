package com.gopay.app.contracts;

import lombok.Data;

@Data
public class TenantTokenResponse {
    private String code;
    private String msg;
    private String tenantAccessToken;
    private String expire;
}
