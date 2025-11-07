package com.admin.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.admin.dto.AdminDashboardStats;
import com.admin.model.Admin;
import com.admin.repository.AdminRepository;
import com.patient.model.Appointment;
import com.patient.model.Doctor;
import com.patient.model.Patient;
import com.patient.repository.AppointmentRepository;
import com.patient.repository.DoctorRepository;
import com.patient.repository.PatientRepository;

@Service
public class AdminService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AdminRepository adminRepository;

    // appointment management

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> getPendingAppointments() {
        return appointmentRepository.findAll().stream()
                .filter(appointment -> "REQUESTED".equals(appointment.getStatus()))
                .collect(Collectors.toList());
    }

    public Appointment approveAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus("APPROVED");
            return appointmentRepository.save(appointment);
        }

        return null;
    }

    public Appointment rejectAppointment(Long appointmentId) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setStatus("REJECTED");
            return appointmentRepository.save(appointment);
        }

        return null;
    }

    public Appointment scheduleAppointment(Long appointmentId, LocalDateTime newDateTime) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isPresent()) {
            Appointment appointment = appointmentOpt.get();
            appointment.setAppointmentDateTime(newDateTime);
            appointment.setStatus("SCHEDULED");
            return appointmentRepository.save(appointment);
        }

        return null;
    }

    // Doctor's availability management
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    public boolean isDoctorAvailable(Long doctorId, LocalDateTime dateTime) {
        List<Appointment> doctorAppointments = appointmentRepository.findByDoctorId(doctorId);
        return doctorAppointments.stream()
                .noneMatch(appointment ->
                    appointment.getAppointmentDateTime().equals(dateTime) &&
                    !"CANCELLED".equals(appointment.getStatus()));
    }

    // User accnullount management
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long patientId, Patient patientDetails) {
        Optional<Patient> patientOpt = patientRepository.findById(patientId);
        if (patientOpt.isPresent()) {
            Patient patient = patientOpt.get();
            patient.setFirstName(patientDetails.getFirstName());
            patient.setLastName(patientDetails.getLastName());
            patient.setEmail(patientDetails.getEmail());
            patient.setPhone(patientDetails.getPhone());
            patient.setDateOfBirth(patientDetails.getDateOfBirth());
            patient.setAddress(patientDetails.getAddress());
            return patientRepository.save(patient);
        }

        return null;
    }

    public boolean deletePatient(Long patientId) {
        if (patientRepository.existsById(patientId)) {
            patientRepository.deleteById(patientId);
            return true;
        }
        return false;
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save (doctor);
    }

    public Doctor updateDoctor(Long doctorId, Doctor doctorDetails) {
        Optional<Doctor> doctorOpt = doctorRepository.findById(doctorId);
        if ( doctorOpt.isPresent() ) {
            Doctor doctor = doctorOpt.get();
            doctor.setFirstName(doctorDetails.getFirstName());
            doctor.setLastName(doctorDetails.getLastName());
            doctor.setSpecialization(doctorDetails.getSpecialization());
            doctor.setQualifications(doctorDetails.getQualifications());
            doctor.setExperienceYears(doctorDetails.getExperienceYears());
            doctor.setEmail(doctorDetails.getEmail());
            doctor.setPhone(doctorDetails.getPhone());
            return doctorRepository.save(doctor);
        }

        return null;
    }

    public boolean deleteDoctor(Long doctorId) {
        if ( doctorRepository.existsById(doctorId) ) {
            doctorRepository.deleteById(doctorId);
            return true;
        }
        return false;
    }

    // Admin authentication (simple)
    public boolean authenticateAdmin(String username, String password) {
        Optional<Admin> adminOpt = adminRepository.findByUsername(username);
        return adminOpt.isPresent() && adminOpt.get().getPassword().equals(password);
    }

    // Dashboard Statistics
    public AdminDashboardStats getDashboardStats() {
        AdminDashboardStats stats = new AdminDashboardStats();
        stats.setTotalPatients(patientRepository.count());
        stats.setTotalDoctors(doctorRepository.count());
        stats.setTotalAppointments(appointmentRepository.count());
        stats.setPendingAppointments((long)getPendingAppointments().size());
        stats.setApprovedAppointments(appointmentRepository.findAll().stream()
                .filter(app -> "APPROVED".equals(app.getStatus()))
                .count());
        return stats;
    }
}
