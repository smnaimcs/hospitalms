import React from 'react';

function KPIs() {
  const sampleStats = {
    Appointments: 120,
    Patients: 45,
    Doctors: 10,
    Revenue: '$20,000',
  };

  return (
    <div className="grid md:grid-cols-4 gap-4">
      {Object.entries(sampleStats).map(([key, value]) => (
        <div
          key={key}
          className="bg-surface p-4 rounded-lg shadow-sm hover:shadow hover:bg-hoverLight transition duration-200"
        >
          <h2 className="text-sm uppercase text-gray-400">{key}</h2>
          <p className="text-xl font-bold text-white">{value}</p>
        </div>
      ))}
    </div>
  );
}

export default KPIs;
