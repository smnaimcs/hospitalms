import React, { useState } from 'react';
import { nanoid } from 'nanoid';

function Invoices() {
  const [invoices, setInvoices] = useState([
    { id: nanoid(), number: 'INV-001', amount: '$200', status: 'Paid' },
    { id: nanoid(), number: 'INV-002', amount: '$150', status: 'Pending' },
    { id: nanoid(), number: 'INV-003', amount: '$300', status: 'Refunded' },
  ]);

  const [newInvoice, setNewInvoice] = useState({
    number: '',
    amount: '',
    status: 'Pending',
  });

  const handleAddInvoice = () => {
    if (!newInvoice.number || !newInvoice.amount) return;
    const newItem = { ...newInvoice, id: nanoid() };
    setInvoices([...invoices, newItem]);
    setNewInvoice({ number: '', amount: '', status: 'Pending' });
  };

  return (
    <div className="space-y-4 p-4">
      <h1 className="text-xl font-semibold mb-4 text-white">Invoices & Payments</h1>

      <div className="bg-surface p-4 rounded shadow grid md:grid-cols-3 gap-4 items-end">
        <input
          type="text"
          placeholder="Invoice Number"
          className="bg-gray-800 text-white border border-gray-700 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary"
          value={newInvoice.number}
          onChange={(e) => setNewInvoice({ ...newInvoice, number: e.target.value })}
        />
        <input
          type="text"
          placeholder="Amount"
          className="bg-gray-800 text-white border border-gray-700 rounded px-3 py-2 focus:outline-none focus:ring-2 focus:ring-primary"
          value={newInvoice.amount}
          onChange={(e) => setNewInvoice({ ...newInvoice, amount: e.target.value })}
        />
        <button
          onClick={handleAddInvoice}
          className="bg-primary text-white px-4 py-2 rounded-full hover:bg-blue-600 transition"
        >
          Add Invoice
        </button>
      </div>

      <table className="min-w-full bg-surface rounded-lg overflow-hidden text-white">
        <thead>
          <tr className="bg-gray-800 text-gray-300">
            <th className="p-2 border-b border-gray-700 text-left">Number</th>
            <th className="p-2 border-b border-gray-700 text-left">Amount</th>
            <th className="p-2 border-b border-gray-700 text-left">Status</th>
          </tr>
        </thead>
        <tbody>
          {invoices.map((inv) => (
            <tr key={inv.id} className="hover:bg-gray-700 transition-colors">
              <td className="p-2 border-b border-gray-700">{inv.number}</td>
              <td className="p-2 border-b border-gray-700">{inv.amount}</td>
              <td className="p-2 border-b border-gray-700">{inv.status}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default Invoices;
