# Patient Management System - API Documentation

## 🚀 Quick Start
- **Base URL**: `http://localhost:8080/api`
- **Backend runs on**: Port 8080
- **Frontend should run on**: Port 3000 (for CORS)

---

## 📋 Core Entities & IDs
- **Patients**: ID 1 (John Doe), ID 2 (Jane Smith)
- **Doctors**: ID 1 (Cardiology), ID 2 (Dermatology), ID 3 (Neurology)

---

## 🔑 API Endpoints

### 1. 🏥 DOCTORS
**Get All Doctors**
```
GET /doctors
```
**Response**: List of all doctors with their qualifications

**Search Doctors** (Smart Search)
```
GET /doctors/search?query=cardio
```
Searches by: name, specialization, qualifications

**Filter by Specialization**
```
GET /doctors/specialization/Cardiology
```

### 2. 📅 APPOINTMENTS
**Request New Appointment**
```
POST /appointments
```
**Body**:
```json
{
  "patientId": 1,
  "doctorId": 1,
  "appointmentDateTime": "2024-01-15T10:00:00"
}
```

**Get Patient's Appointments**
```
GET /appointments/patient/1
```

**Cancel Appointment**
```
PUT /appointments/1/cancel
```

### 3. 🩺 MEDICAL RECORDS
**Get Patient Records**
```
GET /medical-records/patient/1
```

### 4. 👤 PATIENT PROFILE
**Update Patient Info**
```
PUT /patients/1
```
**Body** (send only fields you want to update):
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@email.com",
  "phone": "123-456-7890",
  "address": "123 Main St"
}
```

---

## 🎯 Frontend Integration Examples

### Request an Appointment
```javascript
const appointmentData = {
  patientId: 1,
  doctorId: 2,
  appointmentDateTime: "2024-01-20T14:30:00"
};

const response = await fetch('http://localhost:8080/api/appointments', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(appointmentData)
});
```

### Search Doctors
```javascript
// Smart search across all fields
const doctors = await fetch('http://localhost:8080/api/doctors/search?query=heart')
  .then(res => res.json());

// Filter by specialization only
const cardiologists = await fetch('http://localhost:8080/api/doctors/specialization/Cardiology')
  .then(res => res.json());
```

### Update Patient Profile
```javascript
const updateData = {
  phone: "555-1234",
  address: "456 New Street"
};

await fetch('http://localhost:8080/api/patients/1', {
  method: 'PUT',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(updateData)
});
```

---

## 📊 Sample Data (Pre-loaded)

### Patients
- **ID 1**: John Doe (john.doe@email.com)
- **ID 2**: Jane Smith (jane.smith@email.com)

### Doctors
- **ID 1**: Dr. Michael Johnson - Cardiology (15 years experience)
- **ID 2**: Dr. Sarah Williams - Dermatology (10 years experience)  
- **ID 3**: Dr. Robert Brown - Neurology (20 years experience)

---

## 🛠️ Testing Tips

1. **Start with these IDs**:
   - Use patient ID 1 or 2
   - Use doctor ID 1, 2, or 3

2. **Test appointment flow**:
   ```javascript
   // 1. Get doctors to display
   // 2. Let user select doctor and time
   // 3. POST to /appointments with patientId, doctorId, datetime
   ```

3. **Date format**: Always use `YYYY-MM-DDTHH:mm:ss` (ISO format)

---

## ❗ Common Issues & Solutions

**CORS Error**: Make sure frontend runs on `http://localhost:3000`

**404 Error**: Check if backend is running on port 8080

**Empty response**: Use the sample patient IDs (1 or 2) that have data

**Date parsing**: Use exact format: `2024-01-15T10:00:00`

---

## 🎉 Ready-to-Use Code Snippets

### Load All Doctors for Dropdown
```javascript
async function loadDoctors() {
  const doctors = await fetch('http://localhost:8080/api/doctors')
    .then(res => res.json());
  
  // Use in dropdown: doctor.id as value, doctor name as text
  return doctors;
}
```

### Load Patient's Medical History
```javascript
async function loadMedicalHistory(patientId) {
  const records = await fetch(`http://localhost:8080/api/medical-records/patient/${patientId}`)
    .then(res => res.json());
  return records;
}
```

That's it! Your frontend should work seamlessly with these APIs. Start with the sample IDs provided and you'll see immediate results! 🚀

## 🚀 API Documentation for Doctors

### Base URL: `http://localhost:8080/api/doctor/{doctorId}`

### 1. View Appointments
```
GET /{doctorId}/appointments
```

### 2. View Patient Records
```
GET /{doctorId}/patients/{patientId}/records
```

### 3. Complete Patient History
```
GET /{doctorId}/patients/{patientId}/history
```

### 4. Smart Search
```
GET /{doctorId}/search/patients?query=john
```

### 5. Add Diagnosis
```
POST /{doctorId}/patients/{patientId}/diagnosis
Body: {
  "diagnosisCode": "I10",
  "diagnosisName": "Hypertension",
  "description": "High blood pressure"
}
```

### 6. Prescribe Medicine
```
POST /{doctorId}/patients/{patientId}/prescription
Body: {
  "medicineName": "Lisinopril",
  "dosage": "10mg",
  "frequency": "Once daily",
  "durationDays": 30,
  "instructions": "Take in morning"
}
```

### 7. Get Doctor's Patients
```
GET /{doctorId}/patients
```

---

## 🎯 Testing Instructions

1. **Start the backend** (runs on port 8080)
2. **Open `doctor-dashboard.html`** in browser
3. **Use Doctor ID**: 1, 2, or 3 (sample data pre-loaded)
4. **Test with Patient IDs**: 1 or 2

### Sample Test Flow:
1. Click "My Appointments" - see doctor's scheduled appointments
2. Click "My Patients" - see all patients with appointments
3. Use "Search Patients" - find patients by name/email
4. Click "View Complete History" - see full patient medical history
5. Use "Add Diagnosis" and "Prescribe Medicine" - add new records

The system now supports all doctor functionalities on top of the existing patient system! 🎉


# Admin System Documentation

## 🏥 Admin Dashboard - Complete Management System

### 📋 Overview
The Admin System provides complete management capabilities for the hospital management system. A single admin account manages all appointments, doctors, patients, and system operations.

---

## 🔐 Admin Authentication

### Default Admin Credentials
- **Username**: `admin`
- **Password**: `admin123`

### Login Endpoint
```http
POST /api/admin/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

**Response:**
```json
{
  "status": "Login successful"
}
```

---

## 📊 Dashboard & Statistics

### Get Dashboard Overview
```http
GET /api/admin/dashboard/stats
```

**Response:**
```json
{
  "totalPatients": 45,
  "totalDoctors": 12,
  "totalAppointments": 156,
  "pendingAppointments": 8,
  "approvedAppointments": 132
}
```

---

## 📅 Appointment Management

### 1. View All Appointments
```http
GET /api/admin/appointments
```

**Response:**
```json
[
  {
    "id": 1,
    "appointmentDateTime": "2024-01-15T10:00:00",
    "status": "REQUESTED",
    "patient": {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@email.com",
      "phone": "123-456-7890"
    },
    "doctor": {
      "id": 1,
      "firstName": "Michael",
      "lastName": "Johnson",
      "specialization": "Cardiology"
    }
  }
]
```

### 2. View Pending Appointments
```http
GET /api/admin/appointments/pending
```

### 3. Approve Appointment
```http
PUT /api/admin/appointments/{appointmentId}/approve
```

### 4. Reject Appointment
```http
PUT /api/admin/appointments/{appointmentId}/reject
```

### 5. Schedule/Reschedule Appointment
```http
PUT /api/admin/appointments/schedule
Content-Type: application/json

{
  "appointmentId": 1,
  "newDateTime": "2024-01-20T14:30:00"
}
```

---

## 👨‍⚕️ Doctor Management

### 1. View All Doctors
```http
GET /api/admin/doctors
```

**Response:**
```json
[
  {
    "id": 1,
    "firstName": "Michael",
    "lastName": "Johnson",
    "specialization": "Cardiology",
    "qualifications": "MD, FACC",
    "experienceYears": 15,
    "email": "m.johnson@hospital.com",
    "phone": "123-456-7801"
  }
]
```

### 2. Check Doctor Availability
```http
GET /api/admin/doctors/{doctorId}/availability?dateTime=2024-01-15T10:00:00
```

**Response:** `true` or `false`

### 3. View Doctor's Appointments
```http
GET /api/admin/doctors/{doctorId}/appointments
```

### 4. Add New Doctor
```http
POST /api/admin/doctors
Content-Type: application/json

{
  "firstName": "Sarah",
  "lastName": "Williams",
  "specialization": "Dermatology",
  "qualifications": "MD, FAAD",
  "experienceYears": 10,
  "email": "s.williams@hospital.com",
  "phone": "123-456-7802"
}
```

### 5. Update Doctor Information
```http
PUT /api/admin/doctors/{doctorId}
Content-Type: application/json

{
  "firstName": "Michael",
  "lastName": "Johnson Updated",
  "specialization": "Neurology",
  "qualifications": "MD, PhD",
  "experienceYears": 20,
  "email": "michael.updated@hospital.com",
  "phone": "555-5678"
}
```

### 6. Delete Doctor
```http
DELETE /api/admin/doctors/{doctorId}
```

---

## 👥 Patient Management

### 1. View All Patients
```http
GET /api/admin/patients
```

**Response:**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@email.com",
    "phone": "123-456-7890",
    "dateOfBirth": "1985-05-15",
    "address": "123 Main St, City, State"
  }
]
```

### 2. Add New Patient
```http
POST /api/admin/patients
Content-Type: application/json

{
  "firstName": "Alice",
  "lastName": "Smith",
  "email": "alice.smith@email.com",
  "phone": "123-456-7891",
  "dateOfBirth": "1990-08-22",
  "address": "456 Oak Ave, City, State"
}
```

### 3. Update Patient Information
```http
PUT /api/admin/patients/{patientId}
Content-Type: application/json

{
  "firstName": "Johnny",
  "lastName": "Doe Updated",
  "email": "johnny.doe@email.com",
  "phone": "555-1234",
  "dateOfBirth": "1985-05-15",
  "address": "789 New Street, City, State"
}
```

### 4. Delete Patient
```http
DELETE /api/admin/patients/{patientId}
```

---

## 🔔 Notification System

### Send Appointment Notification
```http
POST /api/admin/notifications/appointment?appointmentId=1&message=Your appointment has been scheduled for tomorrow at 10:00 AM
```

---

## 🎯 Admin Workflows

### 1. Daily Appointment Management Workflow
```
1. Login to Admin Dashboard
2. Check Dashboard Stats for pending appointments
3. Review Pending Appointments (/appointments/pending)
4. For each appointment:
   - Check doctor availability (/doctors/{id}/availability)
   - Approve/Reject based on availability
   - Schedule if needed (/appointments/schedule)
   - Send notifications to patient and doctor
```

### 2. New Doctor Onboarding Workflow
```
1. Add new doctor (/doctors - POST)
2. Set up doctor's schedule and availability
3. Inform relevant departments
4. Update system records
```

### 3. Patient Management Workflow
```
1. Register new patient (/patients - POST)
2. Verify patient information
3. Assign to appropriate doctors if needed
4. Manage patient records and history
```

---

## 📱 Frontend Integration Examples

### Admin Login
```javascript
async function adminLogin(username, password) {
  const response = await fetch('/api/admin/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username, password })
  });
  
  if (response.ok) {
    return await response.text();
  } else {
    throw new Error('Login failed');
  }
}
```

### Load Dashboard Stats
```javascript
async function loadDashboardStats() {
  const response = await fetch('/api/admin/dashboard/stats');
  return await response.json();
}
```

### Manage Appointments
```javascript
async function approveAppointment(appointmentId) {
  const response = await fetch(`/api/admin/appointments/${appointmentId}/approve`, {
    method: 'PUT'
  });
  return await response.json();
}

async function scheduleAppointment(appointmentId, newDateTime) {
  const response = await fetch('/api/admin/appointments/schedule', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ appointmentId, newDateTime })
  });
  return await response.json();
}
```

### Manage Doctors
```javascript
async function addNewDoctor(doctorData) {
  const response = await fetch('/api/admin/doctors', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(doctorData)
  });
  return await response.json();
}

async function checkDoctorAvailability(doctorId, dateTime) {
  const response = await fetch(`/api/admin/doctors/${doctorId}/availability?dateTime=${dateTime}`);
  return await response.json();
}
```

---

## 🚦 Status Codes & Error Handling

### Success Responses
- `200 OK` - Request successful
- `201 Created` - Resource created successfully

### Error Responses
- `400 Bad Request` - Invalid input data
- `401 Unauthorized` - Invalid admin credentials
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server-side error

### Appointment Status Values
- `REQUESTED` - Patient has requested appointment
- `APPROVED` - Admin has approved the appointment
- `REJECTED` - Admin has rejected the appointment
- `SCHEDULED` - Appointment has been scheduled
- `CANCELLED` - Appointment was cancelled
- `COMPLETED` - Appointment has been completed

---

## 🔧 System Requirements

### Backend Requirements
- Java 11+
- Spring Boot 2.7+
- H2 Database (development)
- Maven 3.6+

### Frontend Requirements
- Modern web browser
- HTTP client (Fetch API)
- Bootstrap 5.1+ (for UI components)

### Security Notes
- Single admin account system
- No password encryption in demo (use BCrypt in production)
- CORS enabled for localhost:3000
- No session management in demo version

---

## 🎮 Quick Start Guide

### 1. Initial Setup
```bash
# Start the application
mvn spring-boot:run

# Access: http://localhost:8080
```

### 2. Admin Login
1. Navigate to Admin Dashboard
2. Use credentials: `admin` / `admin123`
3. Access full management capabilities

### 3. Sample Data
The system includes sample:
- 2 Patients (IDs: 1, 2)
- 3 Doctors (IDs: 1, 2, 3)
- Sample appointments with various statuses

### 4. Testing Endpoints
Use Postman or curl to test:
```bash
# Test login
curl -X POST http://localhost:8080/api/admin/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# Test dashboard stats
curl http://localhost:8080/api/admin/dashboard/stats

# Test appointments
curl http://localhost:8080/api/admin/appointments/pending
```

---

## 📞 Support & Troubleshooting

### Common Issues
1. **Login Fails** - Verify admin credentials in database
2. **CORS Errors** - Ensure frontend runs on localhost:3000
3. **Data Not Loading** - Check if sample data is loaded
4. **Appointment Conflicts** - Use doctor availability check

### Log Files
- Check `application.log` for detailed errors
- Enable debug mode: `logging.level.com.patient=DEBUG`

### Database Access
- H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

---

## 🚀 Production Recommendations

### Security Enhancements
- Implement proper password encryption
- Add session management with JWT
- Enable HTTPS
- Add rate limiting

### Additional Features
- Email notifications for appointments
- SMS alerts for urgent matters
- Audit logging for admin actions
- Backup and recovery procedures

This admin system provides complete control over the hospital management platform with a simple, intuitive API structure. 🏥✨
