package com.demo.app.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrescriptionParserTest {

    @Test
    void parseToJson_whenTranscriptNull_returnsErrorJson() {
        PrescriptionParser parser = new PrescriptionParser();

        String json = parser.parseToJson(null);

        assertTrue(json.contains("\"error\": \"No transcript provided\""));
        assertTrue(json.contains("\"medications\": []"));
        assertTrue(json.contains("\"prescriptionDate\": \""));
    }

    @Test
    void parseToJson_extractsPatientDoctorMedicationAndInstructions() {
        PrescriptionParser parser = new PrescriptionParser();

        String transcript = "For John Doe, take Paracetamol 500 mg twice daily for 5 days with food. Dr Smith.";
        String json = parser.parseToJson(transcript);

        assertTrue(json.contains("\"patientName\": \"John Doe\""));
        assertTrue(json.contains("\"doctorName\": \"Smith\""));
        assertTrue(json.contains("\"name\": \"Paracetamol\""));
        assertTrue(json.contains("\"dosage\": \"500 mg\""));
        assertTrue(json.toLowerCase().contains("twice daily"));
        assertTrue(json.toLowerCase().contains("for 5 days"));
        assertTrue(json.toLowerCase().contains("\"instructions\": \"with food\""));
    }
}

