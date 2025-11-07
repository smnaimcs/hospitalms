import React from 'react';

function FAQ() {
  const faqs = [
    { q: 'How to reset password?', a: 'Click on "Forgot Password" at login.' },
    { q: 'How to contact support?', a: 'Use the contact form or chat in the support section.' },
  ];

  return (
    <div className="p-4 bg-surface rounded shadow text-white">
      <h2 className="text-xl mb-4 font-semibold">FAQs & Knowledge Base</h2>
      {faqs.map((faq, idx) => (
        <div key={idx} className="mb-4 border-b border-gray-700 pb-2">
          <h3 className="font-semibold">{faq.q}</h3>
          <p className="mt-1 text-gray-300">{faq.a}</p>
        </div>
      ))}
    </div>
  );
}

export default FAQ;
