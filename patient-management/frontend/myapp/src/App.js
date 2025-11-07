import React from 'react';

function App() {
  const createAppointment = async () => {
    const appointmentData = {
      patientId: 1,
      doctorId: 2,
      appointmentDateTime: "2025-01-01T14:00:00"
    };

    try {
      const response = await fetch("http://localhost:8080/api/appointments", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(appointmentData)
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      console.log("Appointment created:", data);
    } catch (error) {
      console.error("Error creating appointment:", error);
    }
  };

  return (
    <div style={{ padding: "20px" }}>
      <h1>Book Appointment</h1>
      <button onClick={createAppointment}>Create Appointment</button>
    </div>
  );
}

export default App;
