package com.gopay.app.factories;

import com.gopay.app.Server;

public class ServerFactory {
    public static Server createAPIServer() {
        return Server.builder().build();
    }
}
