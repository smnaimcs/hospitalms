/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: ['./src/**/*.{js,jsx,ts,tsx}'],
  theme: {
    extend: {
      colors: {
        background: '#000000',
        surface: '#2A2A2A',
        primary: '#3b82f6',
        textPrimary: '#FFFFFF',
        textSecondary: '#CCCCCC',
        hoverLight: '#3A3A3A',
      },
      fontSize: {
        base: '16px',
        lg: '20px',
        xl: '24px',
        '2xl': '28px',
      },
    },
  },
  plugins: [],
};
