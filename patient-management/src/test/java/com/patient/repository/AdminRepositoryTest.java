package com.patient.repository;

import com.admin.model.Admin;
import com.admin.repository.AdminRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void whenFindByUsername_thenReturnAdmin() {
        // Arrange
        Admin admin = Admin.builder()
                .username("admin")
                .password("admin123")
                .email("admin@hospital.com")
                .firstName("System")
                .lastName("Administrator")
                .active(true)
                .build();
        
        entityManager.persist(admin);
        entityManager.flush();

        // Act
        Optional<Admin> found = adminRepository.findByUsername("admin");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("admin", found.get().getUsername());
        assertEquals("admin@hospital.com", found.get().getEmail());
    }

    @Test
    void whenFindByEmail_thenReturnAdmin() {
        // Arrange
        Admin admin = Admin.builder()
                .username("admin")
                .password("admin123")
                .email("admin@hospital.com")
                .firstName("System")
                .lastName("Administrator")
                .active(true)
                .build();
        
        entityManager.persist(admin);
        entityManager.flush();

        // Act
        Optional<Admin> found = adminRepository.findByEmail("admin@hospital.com");

        // Assert
        assertTrue(found.isPresent());
        assertEquals("admin@hospital.com", found.get().getEmail());
    }

    @Test
    void whenExistsByUsername_thenReturnTrue() {
        // Arrange
        Admin admin = Admin.builder()
                .username("admin")
                .password("admin123")
                .email("admin@hospital.com")
                .firstName("System")
                .lastName("Administrator")
                .active(true)
                .build();
        
        entityManager.persist(admin);
        entityManager.flush();

        // Act
        Boolean exists = adminRepository.existsByUsername("admin");

        // Assert
        assertTrue(exists);
    }

    @Test
    void whenExistsByUsername_withNonExistentUsername_thenReturnFalse() {
        // Act
        Boolean exists = adminRepository.existsByUsername("nonexistent");

        // Assert
        assertFalse(exists);
    }
}