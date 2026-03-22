package com.demo.app;

import com.demo.app.server.PrescriptionServer;
import com.demo.app.service.PrescriptionParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoopHealthApp {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length == 0) {
            // No args — start the REST API server
            startServer();
            return;
        }

        if ("--server".equals(args[0])) {
            startServer();
            return;
        }

        if (args.length == 1 && args[0] != null && !args[0].trim().isEmpty()) {
            // Single arg — run CLI mode (original behavior)
            runOffline(args[0], new PrescriptionParser(), System.out);
            return;
        }

        printUsage();
    }

    private static void startServer() {
        PrescriptionServer server = new PrescriptionServer();
        server.start();
        log.info("REST API ready. Endpoints:");
        log.info("  POST /parse          — Parse transcript (regex). Use ?mode=ai for AI parser.");
        log.info("  POST /parse/ai       — Parse transcript (AI/OpenAI).");
        log.info("  GET  /health         — Health check.");
    }

    static void runOffline(String transcriptOrPath, PrescriptionParser parser, java.io.PrintStream out) throws Exception {
        String transcript = readTranscript(transcriptOrPath);
        out.println("Transcript:");
        out.println(transcript);

        String json = parser.parseToJson(transcript);
        out.println("\nGenerated Prescription JSON:");
        out.println(json);
    }

    private static String readTranscript(String transcriptOrPath) throws Exception {
        java.io.File f = new java.io.File(transcriptOrPath);
        if (f.exists() && f.isFile()) {
            return java.nio.file.Files.readString(f.toPath());
        }
        return transcriptOrPath;
    }

    private static void printUsage() {
        System.err.println("Usage:");
        System.err.println("  Server mode:  ./gradlew run                           (starts REST API on port 8080)");
        System.err.println("  Server mode:  ./gradlew run --args=\"--server\"          (explicit server start)");
        System.err.println("  CLI mode:     ./gradlew run --args=\"<transcript>\"      (parse a transcript string)");
        System.err.println("  CLI mode:     ./gradlew run --args=\"<file.txt>\"        (parse from file)");
        System.err.println();
        System.err.println("Environment variables:");
        System.err.println("  PORT             — Server port (default: 8080)");
        System.err.println("  OPENAI_API_KEY   — Required for AI-based parsing (/parse?mode=ai, /parse/ai)");
    }
}
