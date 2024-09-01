import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import NavBar from "./NavBar";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "Job Finder",
  description: "Job finder website",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" data-theme="mytheme" data-color-mode="light">
      <head>
        <link rel="shortcut icon" href="#" />
      </head>
      <body>
        <NavBar />
        <main 
          className={inter.className}>{children}
        </main>
      </body>
    </html>
  );
}
