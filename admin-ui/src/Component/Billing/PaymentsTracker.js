import React, { useState } from "react";

const initialPayments = [
  { id: "PMT001", patient: "John Doe", amount: "$200", status: "Paid" },
  { id: "PMT002", patient: "Jane Roe", amount: "$150", status: "Pending" },
];

const PaymentsTracker = () => {
  const [payments, setPayments] = useState(initialPayments);

  const updateStatus = (id, status) => {
    setPayments(payments.map(p => (p.id === id ? { ...p, status } : p)));
  };

  return (
    <div className="p-4 bg-surface rounded shadow text-white max-w-lg">
      <h2 className="text-xl mb-4">Payments Tracker</h2>
      <table className="w-full border-collapse border border-gray-700">
        <thead className="bg-gray-900">
          <tr>
            <th className="border border-gray-700 px-3 py-2">Payment ID</th>
            <th className="border border-gray-700 px-3 py-2">Patient</th>
            <th className="border border-gray-700 px-3 py-2">Amount</th>
            <th className="border border-gray-700 px-3 py-2">Status</th>
            <th className="border border-gray-700 px-3 py-2">Update Status</th>
          </tr>
        </thead>
        <tbody>
          {payments.map(({ id, patient, amount, status }) => (
            <tr key={id} className="hover:bg-hoverLight">
              <td className="border border-gray-700 px-3 py-2">{id}</td>
              <td className="border border-gray-700 px-3 py-2">{patient}</td>
              <td className="border border-gray-700 px-3 py-2">{amount}</td>
              <td className="border border-gray-700 px-3 py-2">{status}</td>
              <td className="border border-gray-700 px-3 py-2">
                <select
                  value={status}
                  onChange={e => updateStatus(id, e.target.value)}
                  className="bg-gray-800 rounded p-1 w-full"
                >
                  <option>Paid</option>
                  <option>Pending</option>
                  <option>Refunded</option>
                </select>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default PaymentsTracker;
