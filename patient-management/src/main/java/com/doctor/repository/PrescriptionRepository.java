package com.doctor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.doctor.model.Prescription;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findByDoctorId(Long doctorId);

    @Query("SELECT p FROM Prescription p WHERE p.doctor.id = :doctorId AND p.patient.id = :patientId")
    List<Prescription> findByDoctorAndPatient(@Param("doctorId") Long doctorId, @Param("patientId") Long patientId);
}
