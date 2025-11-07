import React from "react";

const QuickActions = ({ onAddPatient, onSchedule, onGenerateReport }) => (
  <div className="flex space-x-4 mt-4">
    <button
      onClick={onAddPatient}
      className="bg-primary rounded px-4 py-2 hover:bg-blue-600 text-white"
    >
      Add Patient
    </button>
    <button
      onClick={onSchedule}
      className="bg-primary rounded px-4 py-2 hover:bg-blue-600 text-white"
    >
      Schedule Appointment
    </button>
    <button
      onClick={onGenerateReport}
      className="bg-primary rounded px-4 py-2 hover:bg-blue-600 text-white"
    >
      Generate Report
    </button>
  </div>
);

export default QuickActions;
