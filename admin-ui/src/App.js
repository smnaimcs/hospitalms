import React, { useState } from 'react';
import Navigation from './Component/Navigation';
import Dashboard from './Component/Dashboard/Overview';
import Billing from './Component/Billing/Invoices';
import UserManagement from './Component/UserManagement/Users';
import PatientAnalytics from './Component/Analytics/PatientInsights';
import Support from './Component/Support/FAQ';
import QuickActions from "./Component/Dashboard/QuickActions";
import ImportExport from "./Component/Patients/ImportExport";
import DoctorPerformance from './Component/Analytics/DoctorPerformance';
import AppointmentRequests from './Component/Appointments/AppointmentRequests';
import PaymentsTracker from './Component/Billing/PaymentsTracker';
import RevenueReports from './Component/Billing/RevenueReports';
import CRUDPatientForm from './Component/Patients/CRUDPatientForm';
import DoctorSchedule from './Component/Doctors/DoctorSchedule';

function App() {
  const [activeTab, setActiveTab] = useState('dashboard');

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <Dashboard />;
      case 'billing':
        return <Billing />;
      case 'users':
        return <UserManagement />;
      case 'patient-analytics':
        return <PatientAnalytics />;
      case 'support':
        return <Support />;
      case 'quick-actions':
        return <QuickActions />;
      case 'import-export':
        return <ImportExport />;
      case 'doctor-performance':
        return <DoctorPerformance />;
      case 'appointment-requests':
        return <AppointmentRequests />;
      case 'payments-tracker':
        return <PaymentsTracker />;
      case 'revenue-reports':
        return <RevenueReports />;
      case 'crud-patient-form':
        return <CRUDPatientForm />;
      case 'doctor-schedule':
        return <DoctorSchedule />;
      default:
        return <Dashboard />;
    }
  };

  return (
    // <div className="flex flex-col md:flex-row min-h-screen font-sans bg-gray-100">
    <div className="flex flex-col md:flex-row min-h-screen font-sans bg-background">
      <Navigation activeTab={activeTab} setActiveTab={setActiveTab} />
      <main className="flex-1 p-4 overflow-y-auto">{renderContent()}</main>
    </div>
  );
}

export default App;
