const API_BASE_URL = 'http://localhost:8080/api';

// Initialize when page loads
document.addEventListener('DOMContentLoaded', function() {
    loadDoctors();
    loadAllAppointments();
});

// Appointment Functions
async function requestAppointment(event) {
    event.preventDefault();
    
    const appointmentData = {
        patientId: parseInt(document.getElementById('patientId').value),
        doctorId: parseInt(document.getElementById('doctorSelect').value),
        appointmentDateTime: document.getElementById('appointmentDateTime').value
    };

    try {
        const response = await fetch(`${API_BASE_URL}/appointments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(appointmentData)
        });

        if (response.ok) {
            alert('Appointment requested successfully!');
            loadAllAppointments();
            document.getElementById('appointmentForm').reset();
        } else {
            alert('Failed to request appointment');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error requesting appointment');
    }
}

async function loadAllAppointments() {
    try {
        const response = await fetch(`${API_BASE_URL}/appointments`);
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        console.error('Error loading appointments:', error);
    }
}

async function loadPatientAppointments(patientId) {
    try {
        const response = await fetch(`${API_BASE_URL}/appointments/patient/${patientId}`);
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        console.error('Error loading patient appointments:', error);
    }
}

function displayAppointments(appointments) {
    const appointmentList = document.getElementById('appointmentList');
    appointmentList.innerHTML = '';

    if (appointments.length === 0) {
        appointmentList.innerHTML = '<p>No appointments found.</p>';
        return;
    }

    appointments.forEach(appointment => {
        const appointmentDate = new Date(appointment.appointmentDateTime).toLocaleString();
        const card = document.createElement('div');
        card.className = 'card mb-2';
        card.innerHTML = `
            <div class="card-body">
                <h6 class="card-title">Appointment with Dr. ${appointment.doctor.firstName} ${appointment.doctor.lastName}</h6>
                <p class="card-text mb-1"><strong>Date:</strong> ${appointmentDate}</p>
                <p class="card-text mb-1"><strong>Status:</strong> <span class="badge ${getStatusBadgeClass(appointment.status)}">${appointment.status}</span></p>
                <p class="card-text mb-1"><strong>Patient:</strong> ${appointment.patient.firstName} ${appointment.patient.lastName}</p>
                ${appointment.status === 'SCHEDULED' ? 
                    `<button class="btn btn-sm btn-danger mt-2" onclick="cancelAppointment(${appointment.id})">Cancel</button>` : 
                    ''
                }
            </div>
        `;
        appointmentList.appendChild(card);
    });
}

function getStatusBadgeClass(status) {
    switch(status) {
        case 'SCHEDULED': return 'bg-primary';
        case 'CANCELLED': return 'bg-danger';
        case 'COMPLETED': return 'bg-success';
        default: return 'bg-secondary';
    }
}

async function cancelAppointment(appointmentId) {
    if (confirm('Are you sure you want to cancel this appointment?')) {
        try {
            const response = await fetch(`${API_BASE_URL}/appointments/${appointmentId}/cancel`, {
                method: 'PUT'
            });

            if (response.ok) {
                alert('Appointment cancelled successfully!');
                loadAllAppointments();
            } else {
                alert('Failed to cancel appointment');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error cancelling appointment');
        }
    }
}

// Doctor Functions
async function loadDoctors() {
    try {
        const response = await fetch(`${API_BASE_URL}/doctors`);
        const doctors = await response.json();
        displayDoctors(doctors);
        populateDoctorSelect(doctors);
    } catch (error) {
        console.error('Error loading doctors:', error);
    }
}

function displayDoctors(doctors) {
    const doctorsList = document.getElementById('doctorsList');
    doctorsList.innerHTML = '';

    if (doctors.length === 0) {
        doctorsList.innerHTML = '<p>No doctors found.</p>';
        return;
    }

    doctors.forEach(doctor => {
        const card = document.createElement('div');
        card.className = 'card doctor-card';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">Dr. ${doctor.firstName} ${doctor.lastName}</h5>
                <h6 class="card-subtitle mb-2 text-muted">${doctor.specialization}</h6>
                <p class="card-text"><strong>Qualifications:</strong> ${doctor.qualifications}</p>
                <p class="card-text"><strong>Experience:</strong> ${doctor.experienceYears} years</p>
                <p class="card-text"><strong>Contact:</strong> ${doctor.email} | ${doctor.phone}</p>
            </div>
        `;
        doctorsList.appendChild(card);
    });
}

function populateDoctorSelect(doctors) {
    const doctorSelect = document.getElementById('doctorSelect');
    doctorSelect.innerHTML = '<option value="">Select Doctor</option>';
    
    doctors.forEach(doctor => {
        const option = document.createElement('option');
        option.value = doctor.id;
        option.textContent = `Dr. ${doctor.firstName} ${doctor.lastName} - ${doctor.specialization}`;
        doctorSelect.appendChild(option);
    });
}

async function smartSearch() {
    const query = document.getElementById('searchInput').value;
    if (!query.trim()) {
        loadDoctors();
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/doctors/search?query=${encodeURIComponent(query)}`);
        const doctors = await response.json();
        displayDoctors(doctors);
    } catch (error) {
        console.error('Error searching doctors:', error);
    }
}

async function filterBySpecialization(specialization) {
    try {
        let url = `${API_BASE_URL}/doctors`;
        if (specialization) {
            url = `${API_BASE_URL}/doctors/specialization/${specialization}`;
        }
        const response = await fetch(url);
        const doctors = await response.json();
        displayDoctors(doctors);
    } catch (error) {
        console.error('Error filtering doctors:', error);
    }
}

// Medical Records Functions
async function loadMedicalRecords() {
    const patientId = document.getElementById('recordsPatientId').value;
    if (!patientId) {
        alert('Please enter a Patient ID');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/medical-records/patient/${patientId}`);
        const records = await response.json();
        displayMedicalRecords(records);
    } catch (error) {
        console.error('Error loading medical records:', error);
    }
}

function displayMedicalRecords(records) {
    const recordsContainer = document.getElementById('medicalRecords');
    recordsContainer.innerHTML = '';

    if (records.length === 0) {
        recordsContainer.innerHTML = '<p>No medical records found.</p>';
        return;
    }

    records.forEach(record => {
        const recordDate = new Date(record.recordDate).toLocaleDateString();
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">Record from ${recordDate}</h5>
                <p class="card-text"><strong>Doctor:</strong> Dr. ${record.doctor.firstName} ${record.doctor.lastName}</p>
                <p class="card-text"><strong>Diagnosis:</strong> ${record.diagnosis}</p>
                <p class="card-text"><strong>Treatment:</strong> ${record.treatment}</p>
                <p class="card-text"><strong>Medications:</strong> ${record.medications}</p>
                <p class="card-text"><strong>Notes:</strong> ${record.notes}</p>
            </div>
        `;
        recordsContainer.appendChild(card);
    });
}

// Profile Functions
async function updateProfile(event) {
    event.preventDefault();
    
    const patientId = document.getElementById('updatePatientId').value;
    const updateData = {
        firstName: document.getElementById('firstName').value || null,
        lastName: document.getElementById('lastName').value || null,
        email: document.getElementById('email').value || null,
        phone: document.getElementById('phone').value || null,
        address: document.getElementById('address').value || null
    };

    try {
        const response = await fetch(`${API_BASE_URL}/patients/${patientId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updateData)
        });

        if (response.ok) {
            alert('Profile updated successfully!');
            document.getElementById('profileForm').reset();
        } else {
            alert('Failed to update profile');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error updating profile');
    }
}

// Event Listeners
document.getElementById('appointmentForm').addEventListener('submit', requestAppointment);
document.getElementById('profileForm').addEventListener('submit', updateProfile);
document.getElementById('searchInput').addEventListener('keypress', function(e) {
    if (e.key === 'Enter') {
        smartSearch();
    }
});