package com.doctor.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.patient.model.Doctor;
import com.patient.model.Patient;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "diagnoses")
@AllArgsConstructor
@Builder
public class Diagnosis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diagnosisCode;
    private String diagnosisName;
    private String description;
    private LocalDateTime diagnosedDate;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    @JsonIgnore
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    @JsonIgnore
    private Doctor doctor;

    public Diagnosis() {
    }

    public Diagnosis(String diagnosisCode, String diagnosisName, String description, LocalDateTime diagnosedDate,
            Patient patient, Doctor doctor) {
        this.diagnosisCode = diagnosisCode;
        this.diagnosisName = diagnosisName;
        this.description = description;
        this.diagnosedDate = diagnosedDate;
        this.patient = patient;
        this.doctor = doctor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDiagnosedDate() {
        return diagnosedDate;
    }

    public void setDiagnosedDate(LocalDateTime diagnosedDate) {
        this.diagnosedDate = diagnosedDate;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }    
}
