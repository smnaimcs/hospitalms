import React, { useState } from "react";

const initialRequests = [
  { id: 1, patient: "John Doe", doctor: "Dr. Smith", date: "2025-11-10", status: "Pending" },
  { id: 2, patient: "Jane Roe", doctor: "Dr. Adams", date: "2025-11-11", status: "Approved" },
];

function AppointmentRequests() {
  const [requests, setRequests] = useState(initialRequests);

  const updateStatus = (id, newStatus) => {
    setRequests((reqs) =>
      reqs.map((r) => (r.id === id ? { ...r, status: newStatus } : r))
    );
  };

  return (
    <div className="p-4 bg-surface rounded shadow text-white">
      <h2 className="mb-4 text-xl font-semibold">Appointment Requests</h2>
      <table className="w-full table-auto border-collapse border border-gray-700">
        <thead className="bg-gray-900">
          <tr>
            <th className="border border-gray-700 px-3 py-2">Patient</th>
            <th className="border border-gray-700 px-3 py-2">Doctor</th>
            <th className="border border-gray-700 px-3 py-2">Date</th>
            <th className="border border-gray-700 px-3 py-2">Status</th>
            <th className="border border-gray-700 px-3 py-2">Actions</th>
          </tr>
        </thead>
        <tbody>
          {requests.map(({ id, patient, doctor, date, status }) => (
            <tr key={id} className="hover:bg-hoverLight">
              <td className="border border-gray-700 p-2">{patient}</td>
              <td className="border border-gray-700 p-2">{doctor}</td>
              <td className="border border-gray-700 p-2">{date}</td>
              <td className="border border-gray-700 p-2">{status}</td>
              <td className="border border-gray-700 p-2 space-x-2">
                {status === "Pending" && (
                  <>
                    <button
                      onClick={() => updateStatus(id, "Approved")}
                      className="bg-green-600 px-3 py-1 rounded hover:bg-green-700"
                    >
                      Approve
                    </button>
                    <button
                      onClick={() => updateStatus(id, "Rejected")}
                      className="bg-red-600 px-3 py-1 rounded hover:bg-red-700"
                    >
                      Reject
                    </button>
                  </>
                )}
                {status === "Approved" && (
                  <button className="bg-yellow-600 px-3 py-1 rounded" disabled>
                    Approved
                  </button>
                )}
                {status === "Rejected" && (
                  <button className="bg-gray-600 px-3 py-1 rounded" disabled>
                    Rejected
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default AppointmentRequests;
