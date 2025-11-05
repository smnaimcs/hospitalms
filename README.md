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
