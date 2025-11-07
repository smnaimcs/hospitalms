import React from "react";

const ImportExport = ({ onImport, onExport }) => (
  <div className="flex space-x-2">
    <button onClick={onImport} className="bg-primary px-4 py-2 rounded text-white hover:bg-blue-600">Import CSV</button>
    <button onClick={onExport} className="bg-primary px-4 py-2 rounded text-white hover:bg-blue-600">Export CSV</button>
  </div>
);

export default ImportExport;
