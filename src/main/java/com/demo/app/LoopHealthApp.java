package com.demo.app;

import com.demo.app.service.PrescriptionParser;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoopHealthApp {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length != 1 || args[0] == null || args[0].trim().isEmpty()) {
            System.err.println("Usage:");
            System.err.println("  With MCP STT: ./gradlew run --args=\"<transcript_or_transcript_txt_path>\"");
            System.err.println("  Offline text: ./gradlew run --args=\"<transcript_or_transcript_txt_path>\"");
            return;
        }

        String input = args[0];
        runOffline(input, new PrescriptionParser(), System.out);
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
}