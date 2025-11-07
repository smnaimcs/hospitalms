import React from "react";
import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  LineElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
  PointElement,
} from "chart.js";

ChartJS.register(LineElement, CategoryScale, LinearScale, Tooltip, Legend, PointElement);

const data = {
  labels: ["January", "February", "March", "April", "May", "June"],
  datasets: [
    {
      label: "Revenue",
      data: [3000, 4500, 4000, 5000, 6000, 6500],
      fill: false,
      backgroundColor: "#3B82F6",
      borderColor: "#3B82F6",
    },
  ],
};

const options = {
  responsive: true,
  plugins: {
    legend: { labels: { color: "white" } },
  },
  scales: {
    x: { ticks: { color: "white" } },
    y: { ticks: { color: "white" }, beginAtZero: true },
  },
};

const RevenueReports = () => (
  <div className="bg-surface p-4 rounded shadow text-white max-w-lg">
    <h2 className="text-xl mb-4">Revenue Reports</h2>
    <Line data={data} options={options} />
  </div>
);

export default RevenueReports;
