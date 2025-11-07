import React from "react";

const PatientProfile = ({ patient }) => {
  if (!patient) return <p className="text-white">Select a patient to view details.</p>;

  return (
    <div className="p-4 bg-surface rounded shadow text-white max-w-xl">
      <h2 className="font-semibold text-2xl mb-2">{patient.name}</h2>
      <p>Email: {patient.email}</p>
      <p>Phone: {patient.phone}</p>
      <p>Status: {patient.status}</p>
      <p>Address: {patient.address}</p>
      <h3 className="mt-4 font-semibold">Medical History:</h3>
      <p>{patient.medicalHistory || "No history available."}</p>
    </div>
  );
};

export default PatientProfile;
