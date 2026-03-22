
package com.demo.app.service;

import lombok.extern.slf4j.Slf4j;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
public class PrescriptionParser {

    private static final Pattern MEDICATION_PATTERN = Pattern.compile(
            "(?i)(?:take|medication|drug|pill|tablet|capsule)\\s+([a-zA-Z]+(?:\\s+[a-zA-Z]+)*)"
    );

    private static final Pattern DOSAGE_PATTERN = Pattern.compile(
            "(?i)(\\d+(?:\\.\\d+)?)\\s*(?:mg|milligrams?|g|grams?|ml|milliliters?|tablets?|pills?|capsules?)"
    );

    private static final Pattern FREQUENCY_PATTERN = Pattern.compile(
            "(?i)(?:take|every|once|twice|three times|four times)\\s+(?:daily|per day|a day|in the morning|at night|with meals)"
    );

    private static final Pattern DURATION_PATTERN = Pattern.compile(
            "(?i)for\\s+(\\d+)\\s+(?:days?|weeks?|months?)"
    );

    private static final Pattern DOCTOR_PATTERN = Pattern.compile(
            "(?i)(?:dr\\.?|doctor)\\s+([a-zA-Z]+(?:\\s+[a-zA-Z]+)*)"
    );

    private static final Pattern PATIENT_PATTERN = Pattern.compile(
            "(?i)(?:patient|for)\\s+([a-zA-Z]+(?:\\s+[a-zA-Z]+)*)"
    );

    public String parseToJson(String transcript) {
        log.info("Parsing transcript: {}", transcript);

        if (transcript == null || transcript.trim().isEmpty()) {
            log.warn("Empty or null transcript provided");
            return createEmptyPrescriptionJson();
        }

        try {
            PrescriptionData prescription = extractPrescriptionData(transcript);
            return convertToJson(prescription);
        } catch (Exception e) {
            log.error("Error parsing prescription transcript", e);
            return createErrorJson("Failed to parse prescription: " + e.getMessage());
        }
    }

    private PrescriptionData extractPrescriptionData(String transcript) {
        PrescriptionData prescription = new PrescriptionData();

        // Extract patient information
        prescription.patientName = extractPatientName(transcript);

        // Extract doctor information
        prescription.doctorName = extractDoctorName(transcript);

        // Extract medications
        prescription.medications = extractMedications(transcript);

        // Set current date as prescription date
        prescription.prescriptionDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);

        // Extract general instructions
        prescription.instructions = extractInstructions(transcript);

        return prescription;
    }

    private String extractPatientName(String transcript) {
        Matcher matcher = PATIENT_PATTERN.matcher(transcript);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Unknown Patient";
    }

    private String extractDoctorName(String transcript) {
        Matcher matcher = DOCTOR_PATTERN.matcher(transcript);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "Unknown Doctor";
    }

    private List<Medication> extractMedications(String transcript) {
        List<Medication> medications = new ArrayList<>();

        // Split transcript into sentences for better parsing
        String[] sentences = transcript.split("[.!?]+");

        for (String sentence : sentences) {
            Medication medication = parseMedicationFromSentence(sentence.trim());
            if (medication.name != null && !medication.name.isEmpty()) {
                medications.add(medication);
            }
        }

        // If no medications found, try a more lenient approach
        if (medications.isEmpty()) {
            Medication fallback = parseMedicationFromSentence(transcript);
            if (fallback.name != null && !fallback.name.isEmpty()) {
                medications.add(fallback);
            }
        }

        return medications;
    }

    private Medication parseMedicationFromSentence(String sentence) {
        Medication medication = new Medication();

        // Extract medication name
        Matcher medicationMatcher = MEDICATION_PATTERN.matcher(sentence);
        if (medicationMatcher.find()) {
            medication.name = medicationMatcher.group(1).trim();
        }

        // Extract dosage
        Matcher dosageMatcher = DOSAGE_PATTERN.matcher(sentence);
        if (dosageMatcher.find()) {
            medication.dosage = dosageMatcher.group(0).trim();
        }

        // Extract frequency
        Matcher frequencyMatcher = FREQUENCY_PATTERN.matcher(sentence);
        if (frequencyMatcher.find()) {
            medication.frequency = frequencyMatcher.group(0).trim();
        } else {
            medication.frequency = "As directed";
        }

        // Extract duration
        Matcher durationMatcher = DURATION_PATTERN.matcher(sentence);
        if (durationMatcher.find()) {
            medication.duration = durationMatcher.group(0).trim();
        } else {
            medication.duration = "As prescribed";
        }

        return medication;
    }

    private String extractInstructions(String transcript) {
        // Extract any special instructions that don't fit into medication patterns
        String[] commonInstructions = {
                "with food", "on empty stomach", "before bed", "with water",
                "avoid alcohol", "do not drive", "may cause drowsiness"
        };

        List<String> foundInstructions = new ArrayList<>();
        String lowerTranscript = transcript.toLowerCase();

        for (String instruction : commonInstructions) {
            if (lowerTranscript.contains(instruction)) {
                foundInstructions.add(instruction);
            }
        }

        return foundInstructions.isEmpty() ? "Follow doctor's instructions" :
                String.join(", ", foundInstructions);
    }

    private String convertToJson(PrescriptionData prescription) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"prescriptionDate\": \"").append(prescription.prescriptionDate).append("\",\n");
        json.append("  \"patientName\": \"").append(prescription.patientName).append("\",\n");
        json.append("  \"doctorName\": \"").append(prescription.doctorName).append("\",\n");
        json.append("  \"medications\": [\n");

        for (int i = 0; i < prescription.medications.size(); i++) {
            Medication med = prescription.medications.get(i);
            json.append("    {\n");
            json.append("      \"name\": \"").append(med.name).append("\",\n");
            json.append("      \"dosage\": \"").append(med.dosage).append("\",\n");
            json.append("      \"frequency\": \"").append(med.frequency).append("\",\n");
            json.append("      \"duration\": \"").append(med.duration).append("\"\n");
            json.append("    }");
            if (i < prescription.medications.size() - 1) {
                json.append(",");
            }
            json.append("\n");
        }

        json.append("  ],\n");
        json.append("  \"instructions\": \"").append(prescription.instructions).append("\"\n");
        json.append("}");

        return json.toString();
    }

    private String createEmptyPrescriptionJson() {
        return "{\n" +
                "  \"error\": \"No transcript provided\",\n" +
                "  \"prescriptionDate\": \"" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\",\n" +
                "  \"medications\": []\n" +
                "}";
    }

    private String createErrorJson(String errorMessage) {
        return "{\n" +
                "  \"error\": \"" + errorMessage + "\",\n" +
                "  \"prescriptionDate\": \"" + LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "\",\n" +
                "  \"medications\": []\n" +
                "}";
    }

    // Inner classes for data structure
    private static class PrescriptionData {
        String prescriptionDate;
        String patientName;
        String doctorName;
        List<Medication> medications = new ArrayList<>();
        String instructions;
    }

    private static class Medication {
        String name;
        String dosage;
        String frequency;
        String duration;
    }
}