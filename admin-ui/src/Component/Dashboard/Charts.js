import React from 'react';
import { Bar } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from 'chart.js';

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

function Charts() {
  const data = {
    labels: [
      'Cardiology',
      'Neurology',
      'Pediatrics',
      'Dermatology',
      'Orthopedics',
    ],
    datasets: [
      {
        label: 'Patient Count',
        data: [12, 19, 9, 14, 7],
        backgroundColor: 'rgba(59, 130, 246, 0.7)', // Accent blue
      },
    ],
  };

  const options = {
    responsive: true,
    plugins: {
      legend: { labels: { color: '#ccc' } },
      tooltip: {
        backgroundColor: '#2A2A2A',
        titleColor: '#fff',
        bodyColor: '#ccc',
      },
    },
    scales: {
      x: { ticks: { color: '#ccc' }, grid: { color: '#444' } },
      y: { ticks: { color: '#ccc' }, grid: { color: '#444' } },
    },
  };

  return (
    <div className="bg-surface p-4 rounded shadow">
      <h3 className="font-semibold mb-2 text-white">Diagnosis Trends</h3>
      <Bar data={data} options={options} />
    </div>
  );
}

export default Charts;
