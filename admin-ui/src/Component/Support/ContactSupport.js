import React, { useState } from 'react';

function ContactSupport() {
  const [message, setMessage] = useState('');

  const handleSubmit = () => {
    alert('Support message sent!');
    setMessage('');
  };

  return (
    <div className="bg-surface p-4 rounded-lg shadow-sm">
      <h2 className="text-2xl text-white font-semibold mb-4">Contact Support</h2>
      <textarea
        className="w-full p-2 bg-gray-800 text-white border border-gray-700 rounded mb-4 focus:outline-none focus:ring-2 focus:ring-primary"
        rows={4}
        placeholder="Describe your issue..."
        value={message}
        onChange={(e) => setMessage(e.target.value)}
      />
      <button
        className="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition duration-200"
        onClick={handleSubmit}
      >
        Send Message
      </button>
    </div>
  );
}

export default ContactSupport;
