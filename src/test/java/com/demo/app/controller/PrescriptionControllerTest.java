package com.demo.app.controller;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrescriptionControllerTest {

    private Javalin createTestApp() {
        PrescriptionController controller = new PrescriptionController();
        return Javalin.create()
                .post("/parse", controller::parse)
                .post("/parse/ai", controller::parseWithAI)
                .get("/health", controller::healthCheck);
    }

    @Test
    void healthCheck_returnsUpStatus() {
        JavalinTest.test(createTestApp(), (server, client) -> {
            var response = client.get("/health");
            assertEquals(200, response.code());

            String body = response.body().string();
            assertTrue(body.contains("\"status\":\"UP\""));
            assertTrue(body.contains("\"service\":\"loop-health-prescription-parser\""));
        });
    }

    @Test
    void parse_withValidTranscript_returns200() {
        JavalinTest.test(createTestApp(), (server, client) -> {
            String requestBody = "{\"transcript\": \"Dr. Smith prescribed take Amoxicillin 500mg twice daily for 7 days for patient John Doe with food\"}";
            var response = client.post("/parse", requestBody);
            assertEquals(200, response.code());

            String body = response.body().string();
            assertTrue(body.contains("Amoxicillin"));
            assertTrue(body.contains("Smith"));
            assertTrue(body.contains("John Doe"));
            assertTrue(body.contains("with food"));
        });
    }

    @Test
    void parse_withEmptyTranscript_returns400() {
        JavalinTest.test(createTestApp(), (server, client) -> {
            String requestBody = "{\"transcript\": \"\"}";
            var response = client.post("/parse", requestBody);
            assertEquals(400, response.code());

            String body = response.body().string();
            assertTrue(body.contains("Transcript is required"));
        });
    }

    @Test
    void parse_withNullTranscript_returns400() {
        JavalinTest.test(createTestApp(), (server, client) -> {
            String requestBody = "{}";
            var response = client.post("/parse", requestBody);
            assertEquals(400, response.code());
        });
    }

    @Test
    void parse_withMissingBody_returns400() {
        JavalinTest.test(createTestApp(), (server, client) -> {
            var response = client.post("/parse", "");
            // Javalin returns 400 for malformed JSON
            assertTrue(response.code() >= 400);
        });
    }

    @Test
    void parseAI_withMissingApiKey_returns500() {
        // AI parser should fail gracefully when OPENAI_API_KEY is not set
        JavalinTest.test(createTestApp(), (server, client) -> {
            String requestBody = "{\"transcript\": \"Dr. Smith prescribed Amoxicillin for patient John\"}";
            var response = client.post("/parse/ai", requestBody);
            // Should be 500 (missing API key) or 400
            assertTrue(response.code() >= 400);
        });
    }
}
