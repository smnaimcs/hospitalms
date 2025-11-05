package com.patient.repository;

import com.patient.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void whenFindById_thenReturnPatient() {
        // Arrange
        Patient patient = Patient.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@email.com")
                .dateOfBirth(LocalDate.of(1985, 5, 15))
                .build();
        
        Patient savedPatient = entityManager.persistAndFlush(patient);

        // Act
        Optional<Patient> found = patientRepository.findById(savedPatient.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals("Doe", found.get().getLastName());
    }

    @Test
    void whenFindAll_thenReturnAllPatients() {
        // Arrange
        Patient patient1 = Patient.builder().firstName("John").lastName("Doe").email("john@email.com").build();
        Patient patient2 = Patient.builder().firstName("Jane").lastName("Smith").email("jane@email.com").build();
        
        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.flush();

        // Act
        List<Patient> patients = patientRepository.findAll();

        // Assert
        assertEquals(2, patients.size());
    }
}