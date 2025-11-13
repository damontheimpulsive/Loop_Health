package com.gopay.app;

import com.gopay.app.factories.ServerFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) {
        Server server = ServerFactory.createAPIServer();
        server.start();
        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    log.info("Running Shutdown hook...");
                                    server.stop();
                                }));
    }
}
