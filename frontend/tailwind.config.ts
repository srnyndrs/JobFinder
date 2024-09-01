import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./components/**/*.{js,ts,jsx,tsx,mdx}",
    "./app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        "gradient-radial": "radial-gradient(var(--tw-gradient-stops))",
        "gradient-conic":
          "conic-gradient(from 180deg at 50% 50%, var(--tw-gradient-stops))",
      },
    },
  },
  plugins: [
    require('daisyui'),
    require('@tailwindcss/typography')
  ],
  daisyui: {
    themes: [
      "corporate",
      {
        mytheme: {
          "primary": "#304269",
          "secondary": "#304269",         
          "accent": "#F26101",         
          "neutral": "#D9E8F5",         
          "base-100": "#ffffff",        
          "info": "#024059",        
          "success": "#51890f",         
          "warning": "#ff0000",            
          "error": "#ff0000"
        }
      }
    ]
  }
};
export default config;
