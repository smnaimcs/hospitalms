package com.doctor.dto;

import java.util.List;

import com.doctor.model.Diagnosis;
import com.doctor.model.Prescription;
import com.patient.model.Appointment;
import com.patient.model.MedicalRecord;

public class PatientHistoryDTO {
    private List<MedicalRecord> medicalRecords;
    private List<Diagnosis> diagnoses;
    private List<Prescription> prescriptions;
    private List<Appointment> appointments;

    private Long totalRecords;
    private Long totalDiagnoses;
    private Long totalPrescriptions;
    private Long totalAppointments;
    
    public PatientHistoryDTO() {
    }

    public PatientHistoryDTO(List<MedicalRecord> medicalRecords, List<Diagnosis> diagnoses,
            List<Prescription> prescriptions, List<Appointment> appointments) {
        this.medicalRecords = medicalRecords;
        this.diagnoses = diagnoses;
        this.prescriptions = prescriptions;
        this.appointments = appointments;
    }

    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }

    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Long getTotalDiagnoses() {
        return totalDiagnoses;
    }

    public void setTotalDiagnoses(Long totalDiagnoses) {
        this.totalDiagnoses = totalDiagnoses;
    }

    public Long getTotalPrescriptions() {
        return totalPrescriptions;
    }

    public void setTotalPrescriptions(Long totalPrescriptions) {
        this.totalPrescriptions = totalPrescriptions;
    }

    public Long getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(Long totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    
}
