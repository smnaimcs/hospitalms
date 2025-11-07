package com.admin.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.admin.dto.AdminDashboardStats;
import com.admin.dto.AdminLoginRequest;
import com.admin.dto.AppointmentScheduleRequest;
import com.admin.service.AdminService;
import com.patient.model.Appointment;
import com.patient.model.Doctor;
import com.patient.model.Patient;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    
    @Autowired
    private AdminService adminService;

    // Authentication
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminLoginRequest loginRequest) {
        boolean isAuthenticated = adminService.authenticateAdmin(
          loginRequest.getUsername(),
          loginRequest.getPassword()  
        );
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        }
        else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // Dashboard
    @GetMapping("/dashboard/stats")
    public ResponseEntity<AdminDashboardStats> getDashboardStats() {
        AdminDashboardStats stats = adminService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    // Appointment management
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = adminService.getAllAppointments();
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointments/pending")
    public ResponseEntity<List<Appointment>> getPendingAppointments() {
        List<Appointment> appointments = adminService.getPendingAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PutMapping("/appointments/{appointmentId}/approve")
    public ResponseEntity<Appointment> approveAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = adminService.approveAppointment(appointmentId);
        if (appointment != null) {
            return ResponseEntity.ok(appointment);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/appointments/{appointmentId}/reject")
    public ResponseEntity<Appointment> rejectAppointment(@PathVariable Long appointmentId) {
        Appointment appointment = adminService.rejectAppointment(appointmentId);
        if (appointment != null) {
            return ResponseEntity.ok(appointment);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/appointments/schedule")
    public ResponseEntity<Appointment> scheduleAppointment(@RequestBody AppointmentScheduleRequest request) {
        Appointment appointment = adminService.scheduleAppointment(
            request.getAppointmentId(),
            request.getNewDateTime()
        );
        if (appointment != null) {
            return ResponseEntity.ok(appointment);
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // Doctor's Availability

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctor() {
        List<Doctor> doctors = adminService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/{doctorId}/appointments")
    public ResponseEntity<List<Appointment>> getDoctorAppointments(@PathVariable Long doctorId) {
        List<Appointment> appointments = adminService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/doctors/{doctorId}/availability")
    public ResponseEntity<Boolean> checkDoctorAvailability(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime dateTime) {
        boolean isAvailable = adminService.isDoctorAvailable(doctorId, dateTime);
        return ResponseEntity.ok(isAvailable);
    }

    // Patient management
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients() {
        List<Patient> patients = adminService.getAllPatients();
        return ResponseEntity.ok(patients);
    }

    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = adminService.createPatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    @PutMapping("/patients/{patientId}")
    public ResponseEntity<Patient> updatePatient(@PathVariable Long patientId, @RequestBody Patient patientDelails) {
        Patient updatedPatient = adminService.updatePatient(patientId, patientDelails);
        if (updatedPatient != null) {
            return ResponseEntity.ok(updatedPatient);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/patients/{patientId}")
    public ResponseEntity<String> deletePatient(@PathVariable Long patientId) {
        boolean deleted = adminService.deletePatient(patientId);
        if (deleted) {
            return ResponseEntity.ok("Patient deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Doctor management
    @PostMapping("/doctors")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        Doctor savedDoctor = adminService.createDoctor(doctor);
        return ResponseEntity.ok(savedDoctor);
    }

    @PutMapping("/doctors/{doctorId}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long doctorId, @RequestBody Doctor doctorDetails) {
        Doctor updatedDoctor = adminService.updateDoctor(doctorId, doctorDetails);
        if (updatedDoctor != null) {
            return ResponseEntity.ok(updatedDoctor);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/doctors/{doctorId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long doctorId) {
        boolean deleted = adminService.deleteDoctor(doctorId);
        if (deleted) {
            return ResponseEntity.ok("Doctor deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }

    // Send notifications
    @PostMapping("/notifications/appointment")
    public ResponseEntity<String> sendAppointmentNotification(
            @RequestParam Long appointmentId,
            @RequestParam String message) {
        return ResponseEntity.ok("Notification sent successfully");
    }
}
