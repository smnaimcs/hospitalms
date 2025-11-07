import React, { useState } from "react";

const DoctorSchedule = () => {
  const [schedule, setSchedule] = useState([
    { id: 1, day: "Monday", from: "09:00", to: "17:00" },
    { id: 2, day: "Tuesday", from: "10:00", to: "16:00" },
  ]);

  const handleAddSlot = () => {
    setSchedule([...schedule, { id: Date.now(), day: "", from: "", to: "" }]);
  };

  const handleChange = (id, field, value) => {
    setSchedule(schedule.map(slot =>
      slot.id === id ? { ...slot, [field]: value } : slot
    ));
  };

  const handleRemove = (id) => {
    setSchedule(schedule.filter((slot) => slot.id !== id));
  };

  return (
    <div className="p-4 bg-surface rounded shadow text-white max-w-md">
      <h2 className="mb-4 text-xl font-semibold">Doctor Schedule</h2>
      {schedule.map(({ id, day, from, to }) => (
        <div key={id} className="flex items-center space-x-2 mb-2">
          <input
            type="text"
            placeholder="Day"
            value={day}
            onChange={(e) => handleChange(id, "day", e.target.value)}
            className="p-2 rounded bg-gray-800 border border-gray-700 flex-1"
          />
          <input
            type="time"
            value={from}
            onChange={(e) => handleChange(id, "from", e.target.value)}
            className="p-2 rounded bg-gray-800 border border-gray-700 w-24"
          />
          <input
            type="time"
            value={to}
            onChange={(e) => handleChange(id, "to", e.target.value)}
            className="p-2 rounded bg-gray-800 border border-gray-700 w-24"
          />
          <button
            onClick={() => handleRemove(id)}
            className="bg-red-600 px-2 rounded hover:bg-red-700"
          >
            Remove
          </button>
        </div>
      ))}
      <button
        onClick={handleAddSlot}
        className="bg-primary px-4 py-2 rounded hover:bg-blue-600 mt-4"
      >
        Add Slot
      </button>
    </div>
  );
};

export default DoctorSchedule;
