import React, { useState } from "react";

const initialDoctors = [
  { id: 1, name: "Dr Smith", specialty: "Cardiology", available: true },
  { id: 2, name: "Dr Adams", specialty: "Neurology", available: false },
];

const DoctorTable = () => {
  const [doctors, setDoctors] = useState(initialDoctors);
  const [search, setSearch] = useState("");
  
  const filteredDoctors = doctors.filter(d =>
    d.name.toLowerCase().includes(search.toLowerCase()) ||
    d.specialty.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="p-4 bg-surface rounded shadow text-white">
      <input
        type="text"
        placeholder="Search doctors or specialties"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="mb-4 p-2 w-full rounded bg-gray-800 border border-gray-700"
      />
      <table className="w-full border-collapse border border-gray-700">
        <thead className="bg-gray-900">
          <tr>
            <th className="border border-gray-700 px-3 py-2">Name</th>
            <th className="border border-gray-700 px-3 py-2">Specialty</th>
            <th className="border border-gray-700 px-3 py-2">Availability</th>
          </tr>
        </thead>
        <tbody>
          {filteredDoctors.length ?
            filteredDoctors.map(d => (
            <tr key={d.id} className="hover:bg-hoverLight cursor-pointer">
              <td className="border border-gray-700 px-3 py-2">{d.name}</td>
              <td className="border border-gray-700 px-3 py-2">{d.specialty}</td>
              <td className="border border-gray-700 px-3 py-2">{d.available ? "Available" : "Unavailable"}</td>
            </tr>
          )) : (
          <tr>
            <td colSpan={3} className="p-4 text-center italic text-gray-400">No doctors found</td>
          </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default DoctorTable;
