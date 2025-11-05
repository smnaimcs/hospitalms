const API_BASE_URL = 'http://localhost:8080/api';

// Navigation
function showSection(sectionName) {
    // Hide all sections
    document.getElementById('dashboardSection').style.display = 'none';
    document.getElementById('appointmentsSection').style.display = 'none';
    document.getElementById('patientsSection').style.display = 'none';
    document.getElementById('searchSection').style.display = 'none';
    document.getElementById('todaySection').style.display = 'none';
    
    // Show selected section
    document.getElementById(sectionName + 'Section').style.display = 'block';
    
    // Update title and load data
    const titles = {
        'dashboard': 'Dashboard Overview',
        'appointments': 'My Appointments',
        'patients': 'My Patients',
        'search': 'Smart Search',
        'today': 'Today\'s Schedule'
    };
    document.getElementById('sectionTitle').textContent = titles[sectionName];
    
    // Update active nav link
    document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));
    event.target.classList.add('active');
    
    // Load data for section
    switch(sectionName) {
        case 'dashboard':
            loadDashboardStats();
            break;
        case 'appointments':
            loadDoctorAppointments();
            break;
        case 'patients':
            loadDoctorPatients();
            break;
        case 'today':
            loadTodaysAppointments();
            break;
    }
}

// Dashboard Functions
async function loadDashboardStats() {
    const doctorId = document.getElementById('doctorId').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/dashboard/stats`);
        const stats = await response.json();
        
        document.getElementById('statsAppointments').textContent = stats.totalAppointments || 0;
        document.getElementById('statsPatients').textContent = stats.totalPatients || 0;
        document.getElementById('statsDiagnoses').textContent = stats.totalDiagnoses || 0;
        document.getElementById('statsPrescriptions').textContent = stats.totalPrescriptions || 0;
    } catch (error) {
        console.error('Error loading dashboard stats:', error);
    }
}

async function refreshDashboard() {
    await loadDashboardStats();
    showNotification('Dashboard refreshed successfully!', 'success');
}

// Today's Appointments
async function loadTodaysAppointments() {
    const doctorId = document.getElementById('doctorId').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/appointments/today`);
        const appointments = await response.json();
        displayTodaysAppointments(appointments);
    } catch (error) {
        console.error('Error loading today\'s appointments:', error);
    }
}

function displayTodaysAppointments(appointments) {
    const container = document.getElementById('todayAppointmentsList');
    const countElement = document.getElementById('todayAppointmentsCount');
    
    container.innerHTML = '';
    countElement.textContent = appointments.length;

    if (appointments.length === 0) {
        container.innerHTML = `
            <div class="text-center py-4">
                <i class="fas fa-calendar-times fa-3x text-muted mb-3"></i>
                <p class="text-muted">No appointments scheduled for today.</p>
            </div>
        `;
        return;
    }

    appointments.forEach(appointment => {
        const appointmentTime = new Date(appointment.appointmentDateTime).toLocaleTimeString();
        const card = document.createElement('div');
        card.className = 'card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h6 class="card-title mb-1">${appointment.patient.firstName} ${appointment.patient.lastName}</h6>
                        <p class="card-text mb-1"><strong>Time:</strong> ${appointmentTime}</p>
                        <p class="card-text mb-1"><strong>Status:</strong> 
                            <span class="badge bg-success">${appointment.status}</span>
                        </p>
                    </div>
                    <div>
                        <button class="btn btn-sm btn-info me-1" onclick="viewPatientHistory(${appointment.patient.id})">
                            <i class="fas fa-history"></i>
                        </button>
                        <button class="btn btn-sm btn-warning" onclick="updateAppointmentStatus(${appointment.id}, 'COMPLETED')">
                            <i class="fas fa-check"></i>
                        </button>
                    </div>
                </div>
            </div>
        `;
        container.appendChild(card);
    });
}

// Appointments
async function loadDoctorAppointments() {
    const doctorId = document.getElementById('doctorId').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/appointments`);
        const appointments = await response.json();
        displayAppointments(appointments);
    } catch (error) {
        console.error('Error loading appointments:', error);
        showNotification('Error loading appointments', 'error');
    }
}

function displayAppointments(appointments) {
    const container = document.getElementById('appointmentsList');
    container.innerHTML = '';

    if (appointments.length === 0) {
        container.innerHTML = `
            <div class="text-center py-4">
                <i class="fas fa-calendar-times fa-3x text-muted mb-3"></i>
                <p class="text-muted">No appointments found.</p>
            </div>
        `;
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
                        <h5 class="card-title">${appointment.patient.firstName} ${appointment.patient.lastName}</h5>
                        <p class="card-text"><strong>Date & Time:</strong> ${appointmentDate}</p>
                        <p class="card-text"><strong>Status:</strong> 
                            <span class="badge ${getStatusBadgeClass(appointment.status)}">${appointment.status}</span>
                        </p>
                        <p class="card-text"><strong>Contact:</strong> ${appointment.patient.phone} | ${appointment.patient.email}</p>
                    </div>
                    <div class="btn-group">
                        <button class="btn btn-sm btn-info" onclick="viewPatientHistory(${appointment.patient.id})">
                            <i class="fas fa-history me-1"></i>History
                        </button>
                        <button class="btn btn-sm btn-success" onclick="openAddDiagnosis(${appointment.patient.id})">
                            <i class="fas fa-stethoscope me-1"></i>Diagnose
                        </button>
                        <button class="btn btn-sm btn-warning" onclick="openPrescribeMedicine(${appointment.patient.id})">
                            <i class="fas fa-prescription me-1"></i>Prescribe
                        </button>
                    </div>
                </div>
            </div>
        `;
        container.appendChild(card);
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

// Patients
async function loadDoctorPatients() {
    const doctorId = document.getElementById('doctorId').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/patients`);
        const patients = await response.json();
        displayPatients(patients);
    } catch (error) {
        console.error('Error loading patients:', error);
        showNotification('Error loading patients', 'error');
    }
}

function displayPatients(patients) {
    const container = document.getElementById('patientsList');
    container.innerHTML = '';

    if (patients.length === 0) {
        container.innerHTML = `
            <div class="text-center py-4">
                <i class="fas fa-users-slash fa-3x text-muted mb-3"></i>
                <p class="text-muted">No patients found.</p>
            </div>
        `;
        return;
    }

    patients.forEach(patient => {
        const card = document.createElement('div');
        card.className = 'card patient-card mb-3';
        card.innerHTML = `
            <div class="card-body">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <h5 class="card-title">${patient.firstName} ${patient.lastName}</h5>
                        <p class="card-text mb-1"><strong>Email:</strong> ${patient.email}</p>
                        <p class="card-text mb-1"><strong>Phone:</strong> ${patient.phone}</p>
                        ${patient.dateOfBirth ? `<p class="card-text mb-1"><strong>DOB:</strong> ${new Date(patient.dateOfBirth).toLocaleDateString()}</p>` : ''}
                        ${patient.address ? `<p class="card-text mb-1"><strong>Address:</strong> ${patient.address}</p>` : ''}
                    </div>
                    <div class="btn-group-vertical">
                        <button class="btn btn-sm btn-primary mb-1" onclick="viewPatientHistory(${patient.id})">
                            <i class="fas fa-history me-1"></i>Full History
                        </button>
                        <button class="btn btn-sm btn-success mb-1" onclick="openAddDiagnosis(${patient.id})">
                            <i class="fas fa-stethoscope me-1"></i>Add Diagnosis
                        </button>
                        <button class="btn btn-sm btn-warning" onclick="openPrescribeMedicine(${patient.id})">
                            <i class="fas fa-prescription me-1"></i>Prescribe
                        </button>
                    </div>
                </div>
            </div>
        `;
        container.appendChild(card);
    });
}

// Smart Search
async function smartSearch() {
    const doctorId = document.getElementById('doctorId').value;
    const query = document.getElementById('searchQuery').value;
    const searchType = document.getElementById('searchType').value;
    
    if (!query.trim()) {
        showNotification('Please enter a search query', 'warning');
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/search?query=${encodeURIComponent(query)}`);
        const searchResults = await response.json();
        displaySearchResults(searchResults, query, searchType);
    } catch (error) {
        console.error('Error searching:', error);
        showNotification('Error performing search', 'error');
    }
}

function displaySearchResults(results, query, searchType) {
    const container = document.getElementById('searchResults');
    container.innerHTML = '';

    let hasResults = false;

    // Patients
    if ((searchType === 'all' || searchType === 'patients') && results.patients && results.patients.length > 0) {
        hasResults = true;
        const patientSection = document.createElement('div');
        patientSection.innerHTML = `<h6 class="mt-3 text-primary">Patients (${results.patients.length})</h6>`;
        container.appendChild(patientSection);

        results.patients.forEach(patient => {
            const card = document.createElement('div');
            card.className = 'card patient-card mb-2';
            card.onclick = () => showSearchPatientDetail(patient.id);
            card.innerHTML = `
                <div class="card-body py-2">
                    <h6 class="card-title mb-1">${highlightText(patient.firstName + ' ' + patient.lastName, query)}</h6>
                    <p class="card-text mb-0 small">Email: ${highlightText(patient.email, query)}</p>
                    <p class="card-text mb-0 small">Phone: ${patient.phone}</p>
                </div>
            `;
            container.appendChild(card);
        });
    }

    // Diagnoses
    if ((searchType === 'all' || searchType === 'diagnoses') && results.diagnoses && results.diagnoses.length > 0) {
        hasResults = true;
        const diagnosisSection = document.createElement('div');
        diagnosisSection.innerHTML = `<h6 class="mt-3 text-success">Diagnoses (${results.diagnoses.length})</h6>`;
        container.appendChild(diagnosisSection);

        results.diagnoses.forEach(diagnosis => {
            const card = document.createElement('div');
            card.className = 'card mb-2';
            card.innerHTML = `
                <div class="card-body py-2">
                    <h6 class="card-title mb-1">${highlightText(diagnosis.diagnosisName, query)}</h6>
                    <p class="card-text mb-0 small">Code: ${highlightText(diagnosis.diagnosisCode, query)}</p>
                    <p class="card-text mb-0 small">Patient: ${diagnosis.patient.firstName} ${diagnosis.patient.lastName}</p>
                    <small class="text-muted">${new Date(diagnosis.diagnosedDate).toLocaleDateString()}</small>
                </div>
            `;
            container.appendChild(card);
        });
    }

    // Prescriptions
    if ((searchType === 'all' || searchType === 'prescriptions') && results.prescriptions && results.prescriptions.length > 0) {
        hasResults = true;
        const prescriptionSection = document.createElement('div');
        prescriptionSection.innerHTML = `<h6 class="mt-3 text-warning">Prescriptions (${results.prescriptions.length})</h6>`;
        container.appendChild(prescriptionSection);

        results.prescriptions.forEach(prescription => {
            const card = document.createElement('div');
            card.className = 'card mb-2';
            card.innerHTML = `
                <div class="card-body py-2">
                    <h6 class="card-title mb-1">${highlightText(prescription.medicineName, query)}</h6>
                    <p class="card-text mb-0 small">Dosage: ${prescription.dosage}</p>
                    <p class="card-text mb-0 small">Frequency: ${prescription.frequency}</p>
                    <p class="card-text mb-0 small">Patient: ${prescription.patient.firstName} ${prescription.patient.lastName}</p>
                </div>
            `;
            container.appendChild(card);
        });
    }

    if (!hasResults) {
        container.innerHTML = `
            <div class="text-center py-4">
                <i class="fas fa-search fa-3x text-muted mb-3"></i>
                <p class="text-muted">No results found for "${query}"</p>
            </div>
        `;
    }
}

function highlightText(text, query) {
    if (!query) return text;
    const regex = new RegExp(`(${query})`, 'gi');
    return text.replace(regex, '<span class="search-highlight">$1</span>');
}

async function showSearchPatientDetail(patientId) {
    const doctorId = document.getElementById('doctorId').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/patients/${patientId}/history`);
        const history = await response.json();
        displaySearchPatientDetail(history, patientId);
    } catch (error) {
        console.error('Error loading patient detail:', error);
    }
}

function displaySearchPatientDetail(history, patientId) {
    const container = document.getElementById('searchDetail');
    container.innerHTML = `
        <div class="card">
            <div class="card-header">
                <h5>Patient Quick View</h5>
            </div>
            <div class="card-body">
                <div class="text-center mb-3">
                    <button class="btn btn-primary" onclick="viewPatientHistory(${patientId})">
                        <i class="fas fa-history me-2"></i>View Complete History
                    </button>
                </div>
                
                <div class="row text-center mb-3">
                    <div class="col-4">
                        <div class="border rounded p-2">
                            <h6 class="mb-0">${history.medicalRecords.length}</h6>
                            <small>Records</small>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="border rounded p-2">
                            <h6 class="mb-0">${history.diagnoses.length}</h6>
                            <small>Diagnoses</small>
                        </div>
                    </div>
                    <div class="col-4">
                        <div class="border rounded p-2">
                            <h6 class="mb-0">${history.prescriptions.length}</h6>
                            <small>Prescriptions</small>
                        </div>
                    </div>
                </div>
                
                <h6>Recent Medical Activity</h6>
                ${history.medicalRecords.slice(0, 2).map(record => `
                    <div class="border-start border-primary ps-2 mb-2">
                        <strong>${new Date(record.recordDate).toLocaleDateString()}</strong><br>
                        <small>${record.diagnosis}</small>
                    </div>
                `).join('')}
            </div>
        </div>
    `;
}

// Patient History
async function viewPatientHistory(patientId) {
    const doctorId = document.getElementById('doctorId').value;
    
    try {
        console.log("hi");
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/patients/${patientId}/history`);
        const history = await response.json();
        displayPatientHistory(history);
        
        const modal = new bootstrap.Modal(document.getElementById('patientHistoryModal'));
        modal.show();
    } catch (error) {
        console.error('Error loading patient history:', error);
        showNotification('Error loading patient history', 'error');
    }
}

function displayPatientHistory(history) {
    const container = document.getElementById('patientHistoryContent');
    
    container.innerHTML = `
        <div class="row">
            <div class="col-md-4">
                <h5>Medical Records (${history.medicalRecords.length})</h5>
                ${history.medicalRecords.length > 0 ? history.medicalRecords.map(record => `
                    <div class="card mb-3">
                        <div class="card-header">
                            <strong>${new Date(record.recordDate).toLocaleDateString()}</strong>
                        </div>
                        <div class="card-body">
                            <p><strong>Diagnosis:</strong> ${record.diagnosis}</p>
                            <p><strong>Treatment:</strong> ${record.treatment}</p>
                            <p><strong>Medications:</strong> ${record.medications}</p>
                            ${record.notes ? `<p><strong>Notes:</strong> ${record.notes}</p>` : ''}
                            <small class="text-muted">Doctor: ${record.doctor.firstName} ${record.doctor.lastName}</small>
                        </div>
                    </div>
                `).join('') : '<p class="text-muted">No medical records found.</p>'}
            </div>
            
            <div class="col-md-4">
                <h5>Diagnoses (${history.diagnoses.length})</h5>
                ${history.diagnoses.length > 0 ? history.diagnoses.map(diagnosis => `
                    <div class="card mb-3">
                        <div class="card-header">
                            <strong>${diagnosis.diagnosisCode}</strong>
                        </div>
                        <div class="card-body">
                            <h6>${diagnosis.diagnosisName}</h6>
                            <p>${diagnosis.description}</p>
                            <small class="text-muted">Diagnosed: ${new Date(diagnosis.diagnosedDate).toLocaleDateString()}</small>
                        </div>
                    </div>
                `).join('') : '<p class="text-muted">No diagnoses found.</p>'}
            </div>
            
            <div class="col-md-4">
                <h5>Prescriptions (${history.prescriptions.length})</h5>
                ${history.prescriptions.length > 0 ? history.prescriptions.map(prescription => `
                    <div class="card mb-3">
                        <div class="card-header">
                            <strong>${prescription.medicineName}</strong>
                        </div>
                        <div class="card-body">
                            <p><strong>Dosage:</strong> ${prescription.dosage}</p>
                            <p><strong>Frequency:</strong> ${prescription.frequency}</p>
                            <p><strong>Duration:</strong> ${prescription.durationDays} days</p>
                            <p><strong>Instructions:</strong> ${prescription.instructions}</p>
                            <small class="text-muted">Prescribed: ${new Date(prescription.prescribedDate).toLocaleDateString()}</small>
                        </div>
                    </div>
                `).join('') : '<p class="text-muted">No prescriptions found.</p>'}
            </div>
        </div>
    `;
}

// Add Diagnosis
function openAddDiagnosis(patientId) {
    document.getElementById('diagnosisPatientId').value = patientId;
    document.getElementById('diagnosisForm').reset();
    
    const modal = new bootstrap.Modal(document.getElementById('addDiagnosisModal'));
    modal.show();
}

document.getElementById('diagnosisForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const doctorId = document.getElementById('doctorId').value;
    const patientId = document.getElementById('diagnosisPatientId').value;
    
    const diagnosisData = {
        diagnosisCode: document.getElementById('diagnosisCode').value,
        diagnosisName: document.getElementById('diagnosisName').value,
        description: document.getElementById('diagnosisDescription').value
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/patients/${patientId}/diagnosis`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(diagnosisData)
        });
        
        if (response.ok) {
            showNotification('Diagnosis added successfully!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('addDiagnosisModal')).hide();
            // Refresh relevant sections
            loadDashboardStats();
        } else {
            showNotification('Failed to add diagnosis', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error adding diagnosis', 'error');
    }
});

// Prescribe Medicine
function openPrescribeMedicine(patientId) {
    document.getElementById('prescriptionPatientId').value = patientId;
    document.getElementById('prescriptionForm').reset();
    
    const modal = new bootstrap.Modal(document.getElementById('prescribeModal'));
    modal.show();
}

document.getElementById('prescriptionForm').addEventListener('submit', async function(e) {
    e.preventDefault();
    
    const doctorId = document.getElementById('doctorId').value;
    const patientId = document.getElementById('prescriptionPatientId').value;
    
    const prescriptionData = {
        medicineName: document.getElementById('medicineName').value,
        dosage: document.getElementById('dosage').value,
        frequency: document.getElementById('frequency').value,
        durationDays: parseInt(document.getElementById('duration').value),
        instructions: document.getElementById('instructions').value
    };
    
    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/patients/${patientId}/prescription`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(prescriptionData)
        });
        
        if (response.ok) {
            showNotification('Medicine prescribed successfully!', 'success');
            bootstrap.Modal.getInstance(document.getElementById('prescribeModal')).hide();
            // Refresh relevant sections
            loadDashboardStats();
        } else {
            showNotification('Failed to prescribe medicine', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error prescribing medicine', 'error');
    }
});

// Utility Functions
function showNotification(message, type = 'info') {
    // Create a simple notification
    const alertClass = {
        'success': 'alert-success',
        'error': 'alert-danger',
        'warning': 'alert-warning',
        'info': 'alert-info'
    }[type] || 'alert-info';

    const alertDiv = document.createElement('div');
    alertDiv.className = `alert ${alertClass} alert-dismissible fade show position-fixed`;
    alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    document.body.appendChild(alertDiv);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (alertDiv.parentNode) {
            alertDiv.parentNode.removeChild(alertDiv);
        }
    }, 5000);
}

async function updateAppointmentStatus(appointmentId, status) {
    const doctorId = document.getElementById('doctorId').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/doctor/${doctorId}/appointments/${appointmentId}/status?status=${status}`, {
            method: 'PUT'
        });
        
        if (response.ok) {
            showNotification(`Appointment marked as ${status}`, 'success');
            loadTodaysAppointments();
            loadDoctorAppointments();
        } else {
            showNotification('Failed to update appointment status', 'error');
        }
    } catch (error) {
        console.error('Error:', error);
        showNotification('Error updating appointment status', 'error');
    }
}

// Initialize
document.addEventListener('DOMContentLoaded', function() {
    loadDashboardStats();
    loadTodaysAppointments();
    
    // Add event listeners for search
    document.getElementById('searchQuery').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            smartSearch();
        }
    });
});