import React from 'react';
import KPIs from './KPIs';
import Charts from './Charts';

function Overview() {
  return (
    <div className="space-y-6 p-4">
      <h1 className="text-3xl font-semibold text-gray-100 mb-4">
        Dashboard Overview
      </h1>
      <KPIs />
      <Charts />
    </div>
  );
}

export default Overview;
