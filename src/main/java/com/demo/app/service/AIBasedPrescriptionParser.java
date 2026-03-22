
package com.demo.app.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class AIBasedPrescriptionParser {

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String MODEL = "gpt-3.5-turbo";
    private static final String OPENAI_API_KEY = System.getenv("OPENAI_API_KEY");

    public String parseToJson(String transcript) throws Exception {
        if (transcript == null || transcript.trim().isEmpty()) {
            throw new IllegalArgumentException("Transcript must not be null or empty");
        }
        if (OPENAI_API_KEY == null || OPENAI_API_KEY.trim().isEmpty()) {
            throw new IllegalStateException("Missing OPENAI_API_KEY environment variable");
        }
        String userPrompt = "Extract prescription information from this transcript: " + transcript;

        String requestBody = createChatCompletionRequest(transcript, userPrompt);

        HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + OPENAI_API_KEY);
        connection.setRequestProperty("Content-Type", "application/json");

        // Send request
        try (OutputStream output = connection.getOutputStream()) {
            output.write(requestBody.getBytes(StandardCharsets.UTF_8));
        }

        // Get response
        int status = connection.getResponseCode();
        InputStream responseStream = status >= 400 ? connection.getErrorStream() : connection.getInputStream();

        String response = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8);

        if (status >= 400) {
            throw new RuntimeException("OpenAI API error: " + response);
        }

        return extractContentFromResponse(response);
    }

    String createChatCompletionRequest(String systemPrompt, String userPrompt) {
        return "{\n    \"model\": \"" + MODEL + "\",\n    \"messages\": [\n        {\n            \"role\": \"system\",\n            \"content\": \"" + escapeJson(systemPrompt) + "\"\n        },\n        {\n            \"role\": \"user\",\n            \"content\": \"" + escapeJson(userPrompt) + "\"\n        }\n    ],\n    \"temperature\": 0.1,\n    \"max_tokens\": 1500\n}\n";
    }

    String escapeJson(String text) {
        return text.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    String extractContentFromResponse(String response) {
        try {
            // Find the content field in the response
            int contentStart = response.indexOf("\"content\":");
            if (contentStart == -1) {
                throw new RuntimeException("No content found in response: " + response);
            }

            contentStart = response.indexOf("\"", contentStart + 10) + 1;
            int contentEnd = response.indexOf("\"", contentStart);

            // Handle escaped quotes in content
            while (contentEnd > 0 && response.charAt(contentEnd - 1) == '\\') {
                contentEnd = response.indexOf("\"", contentEnd + 1);
            }

            String content = response.substring(contentStart, contentEnd);

            // Unescape JSON
            return content.replace("\\\"", "\"")
                    .replace("\\n", "\n")
                    .replace("\\r", "\r")
                    .replace("\\t", "\t")
                    .replace("\\\\", "\\");

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse OpenAI response: " + response, e);
        }
    }
}