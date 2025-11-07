import React, { useState } from "react";

const initialPatients = [
  { id: 1, name: "John Doe", email: "john@example.com", status: "Active" },
  { id: 2, name: "Jane Smith", email: "jane@example.com", status: "Inactive" },
];

const PatientTable = () => {
  const [patients, setPatients] = useState(initialPatients);
  const [search, setSearch] = useState("");

  const filteredPatients = patients.filter((p) =>
    p.name.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="p-4 bg-surface rounded shadow">
      <input
        type="text"
        placeholder="Search patients"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
        className="mb-4 p-2 w-full rounded bg-gray-800 text-white border border-gray-700"
      />
      <table className="w-full border-collapse border border-gray-700 text-white">
        <thead className="bg-gray-900">
          <tr>
            <th className="border border-gray-700 px-3 py-2">Name</th>
            <th className="border border-gray-700 px-3 py-2">Email</th>
            <th className="border border-gray-700 px-3 py-2">Status</th>
          </tr>
        </thead>
        <tbody>
          {filteredPatients.length ? 
            filteredPatients.map(p => (
            <tr key={p.id} className="hover:bg-hoverLight cursor-pointer">
              <td className="border border-gray-700 px-3 py-2">{p.name}</td>
              <td className="border border-gray-700 px-3 py-2">{p.email}</td>
              <td className="border border-gray-700 px-3 py-2">{p.status}</td>
            </tr>
          )) : (
          <tr>
            <td colSpan="3" className="p-4 text-center text-gray-400 italic">No patients found</td>
          </tr>
          )}
        </tbody>
      </table>
    </div>
  );
};

export default PatientTable;
