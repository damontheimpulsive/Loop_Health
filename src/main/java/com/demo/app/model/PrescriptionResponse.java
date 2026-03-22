package com.demo.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    private String prescriptionDate;
    private String patientName;
    private String doctorName;
    private List<Medication> medications;
    private String instructions;
}
