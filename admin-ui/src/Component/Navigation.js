import React from 'react';

const navItems = [
  { label: 'Overview', id: 'dashboard' },
  { label: 'Quick Actions', id: 'quick-actions' },
  { label: 'Patient Form', id: 'crud-patient-form' },
  { label: 'Doctor Schedule', id: 'doctor-schedule' },
  { label: 'Billing', id: 'billing' },
  { label: 'Payments Tracker', id: 'payments-tracker' },
  { label: 'Revenue Reports', id: 'revenue-reports' },
  { label: 'User Management', id: 'users' },
  { label: 'Patient Analytics', id: 'patient-analytics' },
  { label: 'Doctor Performance', id: 'doctor-performance' },
  { label: 'Appointment Requests', id: 'appointment-requests' },
  { label: 'Import / Export', id: 'import-export' },
  { label: 'Support', id: 'support' },
];

function Navigation({ activeTab, setActiveTab }) {
  return (
    <nav className="w-full md:w-64 bg-surface p-4 space-y-4 rounded-l-lg shadow-lg">
      {navItems.map((item) => (
        <button
          key={item.id}
          className={`w-full text-left px-4 py-2 rounded transition-colors duration-200 ${
            activeTab === item.id
              ? 'bg-gray-700 text-primary font-semibold'
              : 'text-textSecondary hover:bg-hoverLight'
          }`}
          onClick={() => setActiveTab(item.id)}
        >
          {item.label}
        </button>
      ))}
    </nav>
  );
}

export default Navigation;


// import React from 'react';

// const navItems = [
//   { label: 'Overview', id: 'dashboard' },
//   { label: 'Billing', id: 'billing' },
//   { label: 'User Management', id: 'users' },
//   { label: 'Analytics', id: 'analytics' },
//   { label: 'Support', id: 'support' },
// ];

// function Navigation({ activeTab, setActiveTab }) {
//   return (
//     <nav className="w-full md:w-64 bg-surface p-4 space-y-4 rounded-l-lg shadow-lg">
//       {navItems.map((item) => (
//         <button
//           key={item.id}
//           className={`w-full text-left px-4 py-2 rounded transition-colors duration-200 ${
//             activeTab === item.id
//               ? 'bg-gray-700 text-primary font-semibold'
//               : 'text-textSecondary hover:bg-hoverLight'
//           }`}
//           onClick={() => setActiveTab(item.id)}
//         >
//           {item.label}
//         </button>
//       ))}
//     </nav>
//   );
// }

// export default Navigation;
