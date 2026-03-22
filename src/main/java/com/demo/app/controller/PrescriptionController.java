package com.demo.app.controller;

import com.demo.app.model.ErrorResponse;
import com.demo.app.model.ParseRequest;
import com.demo.app.service.AIBasedPrescriptionParser;
import com.demo.app.service.PrescriptionParser;
import com.google.gson.Gson;
import io.javalin.http.Context;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PrescriptionController {

    private final PrescriptionParser regexParser;
    private final AIBasedPrescriptionParser aiParser;
    private final Gson gson;

    public PrescriptionController() {
        this(new PrescriptionParser(), new AIBasedPrescriptionParser());
    }

    public PrescriptionController(PrescriptionParser regexParser, AIBasedPrescriptionParser aiParser) {
        this.regexParser = regexParser;
        this.aiParser = aiParser;
        this.gson = new Gson();
    }

    /**
     * POST /parse — Parse a transcript using regex-based parser.
     * Optionally use ?mode=ai to use the AI-based parser.
     */
    public void parse(Context ctx) {
        ParseRequest request = ctx.bodyAsClass(ParseRequest.class);

        if (request.getTranscript() == null || request.getTranscript().trim().isEmpty()) {
            ctx.status(400).json(new ErrorResponse("Transcript is required", 400));
            return;
        }

        String mode = ctx.queryParam("mode");

        try {
            String json;
            if ("ai".equalsIgnoreCase(mode)) {
                log.info("Parsing transcript using AI-based parser");
                json = aiParser.parseToJson(request.getTranscript());
            } else {
                log.info("Parsing transcript using regex-based parser");
                json = regexParser.parseToJson(request.getTranscript());
            }

            ctx.contentType("application/json").result(json);
        } catch (IllegalStateException e) {
            log.error("Configuration error: {}", e.getMessage());
            ctx.status(500).json(new ErrorResponse(e.getMessage(), 500));
        } catch (Exception e) {
            log.error("Error parsing transcript", e);
            ctx.status(500).json(new ErrorResponse("Failed to parse transcript: " + e.getMessage(), 500));
        }
    }

    /**
     * POST /parse/ai — Parse a transcript using the AI-based parser (OpenAI GPT).
     */
    public void parseWithAI(Context ctx) {
        ParseRequest request = ctx.bodyAsClass(ParseRequest.class);

        if (request.getTranscript() == null || request.getTranscript().trim().isEmpty()) {
            ctx.status(400).json(new ErrorResponse("Transcript is required", 400));
            return;
        }

        try {
            log.info("Parsing transcript using AI-based parser");
            String json = aiParser.parseToJson(request.getTranscript());
            ctx.contentType("application/json").result(json);
        } catch (IllegalStateException e) {
            log.error("Configuration error: {}", e.getMessage());
            ctx.status(500).json(new ErrorResponse(e.getMessage(), 500));
        } catch (Exception e) {
            log.error("Error parsing transcript with AI", e);
            ctx.status(500).json(new ErrorResponse("AI parsing failed: " + e.getMessage(), 500));
        }
    }

    /**
     * GET /health — Health check endpoint.
     */
    public void healthCheck(Context ctx) {
        ctx.json(java.util.Map.of(
                "status", "UP",
                "service", "loop-health-prescription-parser",
                "timestamp", java.time.Instant.now().toString()
        ));
    }
}
