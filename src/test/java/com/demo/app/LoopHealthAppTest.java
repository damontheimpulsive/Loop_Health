package com.demo.app;

import com.demo.app.service.PrescriptionParser;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class LoopHealthAppTest {

    @Test
    void runOffline_usesTranscriptStringAndParses() throws Exception {
        PrescriptionParser parser = new PrescriptionParser();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos);

        LoopHealthApp.runOffline(
                "For John Doe, take Paracetamol 500 mg twice daily for 5 days with food. Dr Smith.",
                parser,
                out
        );

        String printed = baos.toString();
        assertTrue(printed.contains("Transcript:"));
        assertTrue(printed.contains("John Doe"));
        assertTrue(printed.contains("Generated Prescription JSON:"));
        assertTrue(printed.contains("\"patientName\": \"John Doe\""));
        assertTrue(printed.contains("\"name\": \"Paracetamol\""));
    }
}

