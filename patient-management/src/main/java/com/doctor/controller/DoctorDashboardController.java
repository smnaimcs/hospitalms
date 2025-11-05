package com.doctor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doctor.dto.PatientHistoryDTO;
import com.doctor.model.Diagnosis;
import com.doctor.model.Prescription;
import com.doctor.repository.PrescriptionService;
import com.doctor.service.DiagnosisService;
import com.patient.model.Appointment;
import com.patient.model.MedicalRecord;
import com.patient.model.Patient;
import com.patient.service.AppointmentService;
import com.patient.service.MedicalRecordService;
import com.patient.service.PatientService;

@RestController
@RequestMapping("/api/doctor")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorDashboardController {
    
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private DiagnosisService diagnosisService;

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping("/{doctorId}/appointments")
    public List<Appointment> getDoctorAppointments(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsByDoctorId(doctorId);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/records")
    public List<MedicalRecord> getPatientRecords(@PathVariable Long doctorId, @PathVariable Long patientId) {
        return medicalRecordService.getMedicalRecordsByPatientId(patientId);
    }

    @GetMapping("/{doctorId}/patients/{patientId}/history")
    public ResponseEntity<PatientHistoryDTO> getPatientHistory(@PathVariable Long doctorId, @PathVariable Long patientId) {
        PatientHistoryDTO history = new PatientHistoryDTO();
        history.setMedicalRecords(medicalRecordService.getMedicalRecordsByPatientId(patientId));
        history.setDiagnoses(diagnosisService.getDiagnosesByPatientId(patientId));
        history.setPrescriptions(prescriptionService.getPrescriptionsByPatientId(patientId));
        history.setAppointments(appointmentService.getAppointmentsByPatientId(patientId));

        return ResponseEntity.ok(history);
    }

    @GetMapping("/{doctorId}/search/patients")
    public List<Patient> searchPatients(@PathVariable Long doctorId, @RequestParam String query) {
        return patientService.searchPatients(query);
    }

    @PostMapping("/{doctorId}/patients/{patientId}/diagnosis")
    public ResponseEntity<Diagnosis> addDiagnosis(@PathVariable Long doctorId,
                                                  @PathVariable Long patientId,
                                                  @RequestBody Diagnosis diagnosis) {
        Diagnosis savedDiagnosis = diagnosisService.addDiagnosis(doctorId, patientId, diagnosis);
        if (savedDiagnosis != null) {
            return ResponseEntity.ok(savedDiagnosis);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/{doctorId}/patients/{patientId}/prescription")
    public ResponseEntity<Prescription> prescribeMedicine(
        @PathVariable Long doctorId,
        @PathVariable Long patientId,
        @RequestBody Prescription prescription
    ) {
        Prescription savedPrescription = prescriptionService.prescribeMedicine(doctorId, patientId, prescription);
        if (savedPrescription != null) {
            return ResponseEntity.ok(savedPrescription);
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/{doctorId}/patients")
    public List<Patient> getDoctorPatients(@PathVariable Long doctorId) {
        return patientService.getPatientsByDoctorId(doctorId);
    }

    @GetMapping("/{doctorId}/dashboard/stats")
    public ResponseEntity<Map<String, Long>> getDashboardStats(@PathVariable Long doctorId) {
        try {
            Map<String, Long> stats = new HashMap<>();
            
            Long appointmentCount = (long) appointmentService.getAppointmentsByDoctorId(doctorId).size();
            Long patientCount = (long) patientService.getPatientsByDoctorId(doctorId).size();
            Long diagnosisCount = (long) diagnosisService.getDiagnosesByDoctorId(doctorId).size();
            Long prescriptionCount = (long) prescriptionService.getPrescriptionsByDoctorId(doctorId).size();
            
            stats.put("totalAppointments", appointmentCount);
            stats.put("totalPatients", patientCount);
            stats.put("totalDiagnoses", diagnosisCount);
            stats.put("totalPrescriptions", prescriptionCount);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{doctorId}/appointments/today")
    public ResponseEntity<List<Appointment>> getTodayAppointments(@PathVariable Long doctorId) {
        try {
            List<Appointment> allAppointments = appointmentService.getAppointmentsByDoctorId(doctorId);
            List<Appointment> todayAppointments = allAppointments.stream()
                    .filter(appointment -> 
                        appointment.getAppointmentDateTime().toLocalDate().equals(java.time.LocalDate.now()) &&
                        "SCHEDULED".equals(appointment.getStatus()))
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(todayAppointments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{doctorId}/appointments/{appointmentId}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId,
            @RequestParam String status) {
        try {
            // This would need to be implemented in AppointmentService
            // For now, let's create a simple implementation
            Appointment appointment = appointmentService.getAppointmentsByDoctorId(doctorId).stream()
                    .filter(a -> a.getId().equals(appointmentId))
                    .findFirst()
                    .orElse(null);
            
            if (appointment != null) {
                appointment.setStatus(status);
                // You would typically save this back to the database
                return ResponseEntity.ok(appointment);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
