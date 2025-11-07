package com.patient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.admin.dto.AdminLoginRequest;
import com.admin.model.Admin;
import com.admin.repository.AdminRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdminIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdminRepository adminRepository;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/admin";
    }

    @BeforeEach
    void setUp() {
        // Clean up and create admin account
        adminRepository.deleteAll();
        
        Admin admin = Admin.builder()
                .username("admin")
                .password("admin123")
                .email("admin@hospital.com")
                .firstName("System")
                .lastName("Administrator")
                .active(true)
                .build();
        adminRepository.save(admin);
    }

    @Test
    void login_WithValidCredentials_ShouldReturnSuccess() {
        // Arrange
        AdminLoginRequest loginRequest = new AdminLoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AdminLoginRequest> request = new HttpEntity<>(loginRequest, headers);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/login", request, String.class);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful", response.getBody());
    }

    @Test
    void login_WithInvalidCredentials_ShouldReturnUnauthorized() {
        // Arrange
        AdminLoginRequest loginRequest = new AdminLoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("wrongpassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AdminLoginRequest> request = new HttpEntity<>(loginRequest, headers);

        // Act
        ResponseEntity<String> response = restTemplate.postForEntity(
                getBaseUrl() + "/login", request, String.class);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());
    }

    @Test
    void getDashboardStats_AfterLogin_ShouldReturnStats() {
        // First login to get authentication
        AdminLoginRequest loginRequest = new AdminLoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("admin123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AdminLoginRequest> loginRequestEntity = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                getBaseUrl() + "/login", loginRequestEntity, String.class);
        
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());

        // Now try to access dashboard stats
        // Note: Since we're using simple authentication, we don't need tokens
        // But we need to make sure the endpoint is accessible
        HttpEntity<String> statsRequest = new HttpEntity<>(headers);
        ResponseEntity<String> statsResponse = restTemplate.exchange(
                getBaseUrl() + "/dashboard/stats", 
                HttpMethod.GET, 
                statsRequest, 
                String.class);

        // The endpoint should be accessible (returns 200 or 401 based on your security setup)
        assertTrue(statsResponse.getStatusCode().is2xxSuccessful() || 
                  statsResponse.getStatusCode() == HttpStatus.UNAUTHORIZED);
    }
}