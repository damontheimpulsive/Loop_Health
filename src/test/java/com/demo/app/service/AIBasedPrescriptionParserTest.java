package com.demo.app.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AIBasedPrescriptionParserTest {

    @Test
    void escapeJson_escapesQuotesAndNewlines() {
        AIBasedPrescriptionParser parser = new AIBasedPrescriptionParser();

        String escaped = parser.escapeJson("a\"b\nc");

        assertEquals("a\\\"b\\nc", escaped);
    }

    @Test
    void createChatCompletionRequest_includesModelAndMessages() {
        AIBasedPrescriptionParser parser = new AIBasedPrescriptionParser();

        String body = parser.createChatCompletionRequest("system", "user");

        assertTrue(body.contains("\"model\":"));
        assertTrue(body.contains("\"role\": \"system\""));
        assertTrue(body.contains("\"role\": \"user\""));
    }

    @Test
    void extractContentFromResponse_returnsUnescapedContent() {
        AIBasedPrescriptionParser parser = new AIBasedPrescriptionParser();

        String response = "{\n" +
                "  \"choices\": [\n" +
                "    {\n" +
                "      \"message\": {\n" +
                "        \"role\": \"assistant\",\n" +
                "        \"content\": \"{\\\"a\\\": 1}\\nline2\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String content = parser.extractContentFromResponse(response);

        assertEquals("{\"a\": 1}\nline2", content);
    }
}

