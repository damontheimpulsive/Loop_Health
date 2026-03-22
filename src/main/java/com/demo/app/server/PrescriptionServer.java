package com.demo.app.server;

import com.demo.app.controller.PrescriptionController;
import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrescriptionServer {

    private final Javalin app;
    private final int port;

    public PrescriptionServer() {
        this(getConfiguredPort());
    }

    public PrescriptionServer(int port) {
        this.port = port;
        PrescriptionController controller = new PrescriptionController();
        this.app = createApp(controller);
    }

    PrescriptionServer(int port, PrescriptionController controller) {
        this.port = port;
        this.app = createApp(controller);
    }

    private Javalin createApp(PrescriptionController controller) {
        Javalin javalin = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.useVirtualThreads = false;
        });

        // Routes
        javalin.post("/parse", controller::parse);
        javalin.post("/parse/ai", controller::parseWithAI);
        javalin.get("/health", controller::healthCheck);

        // Global exception handler
        javalin.exception(Exception.class, (e, ctx) -> {
            log.error("Unhandled exception", e);
            ctx.status(500).json(java.util.Map.of(
                    "error", "Internal server error",
                    "status", 500
            ));
        });

        return javalin;
    }

    public void start() {
        app.start(port);
        log.info("Loop Health Prescription Server started on port {}", port);
    }

    public void stop() {
        app.stop();
        log.info("Loop Health Prescription Server stopped");
    }

    public Javalin getApp() {
        return app;
    }

    private static int getConfiguredPort() {
        String portEnv = System.getenv("PORT");
        if (portEnv != null) {
            try {
                return Integer.parseInt(portEnv);
            } catch (NumberFormatException e) {
                // fall through to default
            }
        }
        return 8080;
    }
}
