const API_BASE_URL = 'http://localhost:8080/api/admin';
let isLoggedIn = false;

// Login Functionality
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });
        
        if (response.ok) {
            isLoggedIn = true;
            document.getElementById('loginSection').style.display = 'none';
            document.getElementById('dashboardSection').style.display = 'flex';
            loadDashboardStats();
        } else {
            alert('Login failed! Check credentials.');
        }
    } catch (error) {
        console.error('Login error:', error);
        alert('Login error!');
    }
});

// Navigation
function showSection(sectionName) {
    if (!isLoggedIn) return;
    
    // Hide all sections
    document.getElementById('overviewSection').style.display = 'none';
    document.getElementById('appointmentsSection').style.display = 'none';
    document.getElementById('doctorsSection').style.display = 'none';
    document.getElementById('patientsSection').style.display = 'none';
    
    // Show selected section
    document.getElementById(sectionName + 'Section').style.display = 'block';
    
    // Update active nav
    document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
    event.target.classList.add('active');
    
    // Update title and load data
    const titles = {
        'overview': 'Dashboard Overview',
        'appointments': 'Appointment Management',
        'doctors': 'Doctor Management',
        'patients': 'Patient Management'
    };
    document.getElementById('sectionTitle').textContent = titles[sectionName];
    
    // Load section data
    switch(sectionName) {
        case 'appointments':
            loadAllAppointments();
            break;
        case 'doctors':
            loadAllDoctors();
            break;
        case 'patients':
            loadAllPatients();
            break;
    }
}

// Dashboard Stats
async function loadDashboardStats() {
    try {
        const response = await fetch(`${API_BASE_URL}/dashboard/stats`);
        const stats = await response.json();
        
        document.getElementById('statsPatients').textContent = stats.totalPatients;
        document.getElementById('statsDoctors').textContent = stats.totalDoctors;
        document.getElementById('statsAppointments').textContent = stats.totalAppointments;
        document.getElementById('statsPending').textContent = stats.pendingAppointments;
        document.getElementById('statsApproved').textContent = stats.approvedAppointments;
    } catch (error) {
        console.error('Error loading stats:', error);
    }
}

// Appointment Management
async function loadAllAppointments() {
    try {
        const response = await fetch(`${API_BASE_URL}/appointments`);
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        console.error('Error loading appointments:', error);
    }
}

async function loadPendingAppointments() {
    try {
        const response = await fetch(`${API_BASE_URL}/appointments/pending`);
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        console.error('Error loading pending appointments:', error);
    }
}

function displayAppointments(appointments) {
    const container = document.getElementById('appointmentsList');
    container.innerHTML = '';

    if (appointments.length === 0) {
        container.innerHTML = '<p class="text-muted">No appointments found.</p>';
        return;
    }

    appointments.forEach(appointment => {
        const appointmentDate = new Date(appointment.appointmentDateTime).toLocaleString();
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h6 class="card-title">${appointment.patient.firstName} ${appointment.patient.lastName}</h6>
                        <p class="card-text mb-1">With Dr. ${appointment.doctor.firstName} ${appointment.doctor.lastName}</p>
                        <p class="card-text mb-1"><strong>Date:</strong> ${appointmentDate}</p>
                        <p class="card-text mb-1">
                            <strong>Status:</strong> 
                            <span class="badge ${getStatusBadgeClass(appointment.status)}">${appointment.status}</span>
                        </p>
                    </div>
                    <div class="btn-group">
                        ${appointment.status === 'REQUESTED' ? `
                            <button class="btn btn-sm btn-success" onclick="approveAppointment(${appointment.id})">
                                <i class="fas fa-check"></i> Approve
                            </button>
                            <button class="btn btn-sm btn-danger" onclick="rejectAppointment(${appointment.id})">
                                <i class="fas fa-times"></i> Reject
                            </button>
                            <button class="btn btn-sm btn-primary" onclick="openScheduleModal(${appointment.id})">
                                <i class="fas fa-calendar"></i> Schedule
                            </button>
                        ` : ''}
                    </div>
                </div>
            </div>
        `;
        container.appendChild(card);
    });
}

function getStatusBadgeClass(status) {
    switch(status) {
        case 'REQUESTED': return 'bg-warning';
        case 'APPROVED': return 'bg-info';
        case 'SCHEDULED': return 'bg-primary';
        case 'REJECTED': return 'bg-danger';
        case 'COMPLETED': return 'bg-success';
        case 'CANCELLED': return 'bg-secondary';
        default: return 'bg-secondary';
    }
}

async function approveAppointment(appointmentId) {
    try {
        const response = await fetch(`${API_BASE_URL}/appointments/${appointmentId}/approve`, {
            method: 'PUT'
        });
        
        if (response.ok) {
            alert('Appointment approved!');
            loadAllAppointments();
            loadDashboardStats();
        } else {
            alert('Failed to approve appointment');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error approving appointment');
    }
}

async function rejectAppointment(appointmentId) {
    if (confirm('Are you sure you want to reject this appointment?')) {
        try {
            const response = await fetch(`${API_BASE_URL}/appointments/${appointmentId}/reject`, {
                method: 'PUT'
            });
            
            if (response.ok) {
                alert('Appointment rejected!');
                loadAllAppointments();
                loadDashboardStats();
            } else {
                alert('Failed to reject appointment');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error rejecting appointment');
        }
    }
}

function openScheduleModal(appointmentId) {
    document.getElementById('scheduleAppointmentId').value = appointmentId;
    const modal = new bootstrap.Modal(document.getElementById('scheduleModal'));
    modal.show();
}

document.getElementById('scheduleForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const appointmentId = document.getElementById('scheduleAppointmentId').value;
    const newDateTime = document.getElementById('newAppointmentDateTime').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/appointments/schedule`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ appointmentId, newDateTime })
        });
        
        if (response.ok) {
            alert('Appointment scheduled!');
            bootstrap.Modal.getInstance(document.getElementById('scheduleModal')).hide();
            loadAllAppointments();
            loadDashboardStats();
        } else {
            alert('Failed to schedule appointment');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error scheduling appointment');
    }
});

// Doctor Management
async function loadAllDoctors() {
    try {
        const response = await fetch(`${API_BASE_URL}/doctors`);
        const doctors = await response.json();
        displayDoctors(doctors);
    } catch (error) {
        console.error('Error loading doctors:', error);
    }
}

function displayDoctors(doctors) {
    const container = document.getElementById('doctorsList');
    container.innerHTML = '';

    doctors.forEach(doctor => {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">Dr. ${doctor.firstName} ${doctor.lastName}</h5>
                <p class="card-text"><strong>Specialization:</strong> ${doctor.specialization}</p>
                <p class="card-text"><strong>Qualifications:</strong> ${doctor.qualifications}</p>
                <p class="card-text"><strong>Experience:</strong> ${doctor.experienceYears} years</p>
                <p class="card-text"><strong>Contact:</strong> ${doctor.email} | ${doctor.phone}</p>
                <button class="btn btn-sm btn-danger" onclick="deleteDoctor(${doctor.id})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </div>
        `;
        container.appendChild(card);
    });
}

// Patient Management
async function loadAllPatients() {
    try {
        const response = await fetch(`${API_BASE_URL}/patients`);
        const patients = await response.json();
        displayPatients(patients);
    } catch (error) {
        console.error('Error loading patients:', error);
    }
}

function displayPatients(patients) {
    const container = document.getElementById('patientsList');
    container.innerHTML = '';

    patients.forEach(patient => {
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <h5 class="card-title">${patient.firstName} ${patient.lastName}</h5>
                <p class="card-text"><strong>Email:</strong> ${patient.email}</p>
                <p class="card-text"><strong>Phone:</strong> ${patient.phone}</p>
                <p class="card-text"><strong>DOB:</strong> ${patient.dateOfBirth}</p>
                <p class="card-text"><strong>Address:</strong> ${patient.address}</p>
                <button class="btn btn-sm btn-danger" onclick="deletePatient(${patient.id})">
                    <i class="fas fa-trash"></i> Delete
                </button>
            </div>
        `;
        container.appendChild(card);
    });
}

// Logout
function logout() {
    isLoggedIn = false;
    document.getElementById('dashboardSection').style.display = 'none';
    document.getElementById('loginSection').style.display = 'block';
    document.getElementById('loginForm').reset();
}