/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#E6F0FF',
          100: '#B3D4FF',
          200: '#80B8FF',
          300: '#4D9CFF',
          400: '#1A80FF',
          500: '#0A1628',
          600: '#081222',
          700: '#060E1C',
          800: '#040A16',
          900: '#020610',
        },
        accent: {
          50: '#E0FFFA',
          100: '#B3FFF2',
          200: '#80FFE9',
          300: '#4DFFE0',
          400: '#1AFFD7',
          500: '#00F5D4',
          600: '#00D4B8',
          700: '#00B39C',
          800: '#009280',
          900: '#007164',
        },
        dark: {
          bg: '#0A1628',
          card: '#132038',
          border: '#1E3A5F',
          text: '#E2E8F0',
          textMuted: '#94A3B8',
        }
      },
      backgroundColor: {
        'dark-bg': '#0A1628',
        'dark-card': '#132038',
        'dark-hover': '#1E3A5F',
      },
      borderColor: {
        'dark-border': '#1E3A5F',
      },
      textColor: {
        'dark-text': '#E2E8F0',
        'dark-muted': '#94A3B8',
      },
      boxShadow: {
        'glow': '0 0 20px rgba(0, 245, 212, 0.3)',
        'glow-lg': '0 0 40px rgba(0, 245, 212, 0.4)',
        'card': '0 4px 6px -1px rgba(0, 0, 0, 0.3), 0 2px 4px -1px rgba(0, 0, 0, 0.2)',
      },
      animation: {
        'pulse-glow': 'pulse-glow 2s ease-in-out infinite',
        'float': 'float 3s ease-in-out infinite',
      },
      keyframes: {
        'pulse-glow': {
          '0%, 100%': { boxShadow: '0 0 20px rgba(0, 245, 212, 0.3)' },
          '50%': { boxShadow: '0 0 40px rgba(0, 245, 212, 0.6)' },
        },
        'float': {
          '0%, 100%': { transform: 'translateY(0)' },
          '50%': { transform: 'translateY(-10px)' },
        },
      },
    },
  },
  plugins: [],
}
