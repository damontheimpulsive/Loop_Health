package com.gopay.app.contracts;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TenantTokenRequestBody {
    private String appId;
    private String appSecret;
}
