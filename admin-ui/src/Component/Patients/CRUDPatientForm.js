import React, { useState, useEffect } from "react";

const emptyPatient = {
  name: "",
  email: "",
  phone: "",
  status: "Active",
  address: "",
  medicalHistory: "",
};

const CRUDPatientForm = ({ patientToEdit, onSave, onCancel }) => {
  const [patient, setPatient] = useState(emptyPatient);

  useEffect(() => {
    if (patientToEdit) setPatient(patientToEdit);
  }, [patientToEdit]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setPatient((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSave(patient);
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-surface p-6 rounded shadow max-w-lg mx-auto text-white"
    >
      <h2 className="text-xl mb-4">
        {patientToEdit ? "Edit Patient" : "Add Patient"}
      </h2>

      {/* Input fields as per earlier CRUDPatientForm */}

      <label className="block mb-2">
        Name
        <input
          type="text"
          name="name"
          value={patient.name}
          onChange={handleChange}
          required
          className="w-full p-2 mt-1 rounded bg-gray-800 border border-gray-700"
        />
      </label>

      <label className="block mb-2">
        Email
        <input
          type="email"
          name="email"
          value={patient.email}
          onChange={handleChange}
          required
          className="w-full p-2 mt-1 rounded bg-gray-800 border border-gray-700"
        />
      </label>

      <label className="block mb-2">
        Phone
        <input
          type="tel"
          name="phone"
          value={patient.phone}
          onChange={handleChange}
          className="w-full p-2 mt-1 rounded bg-gray-800 border border-gray-700"
        />
      </label>

      <label className="block mb-2">
        Status
        <select
          name="status"
          value={patient.status}
          onChange={handleChange}
          className="w-full p-2 mt-1 rounded bg-gray-800 border border-gray-700"
        >
          <option>Active</option>
          <option>Inactive</option>
        </select>
      </label>

      <label className="block mb-2">
        Address
        <textarea
          name="address"
          value={patient.address}
          onChange={handleChange}
          className="w-full p-2 mt-1 rounded bg-gray-800 border border-gray-700"
        />
      </label>

      <label className="block mb-4">
        Medical History
        <textarea
          name="medicalHistory"
          value={patient.medicalHistory}
          onChange={handleChange}
          className="w-full p-2 mt-1 rounded bg-gray-800 border border-gray-700"
        />
      </label>

      <div className="flex justify-end space-x-4">
        <button
          type="button"
          onClick={onCancel}
          className="bg-gray-600 px-4 py-2 rounded hover:bg-gray-700"
        >
          Cancel
        </button>
        <button
          type="submit"
          className="bg-primary px-4 py-2 rounded hover:bg-blue-600"
        >
          Save
        </button>
      </div>
    </form>
  );
};

export default CRUDPatientForm;
