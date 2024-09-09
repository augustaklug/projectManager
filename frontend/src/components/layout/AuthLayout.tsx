// src/components/layout/AuthLayout.tsx
import React from 'react';
import Navbar from './Navbar';

interface AuthLayoutProps {
  children: React.ReactNode;
}

const AuthLayout: React.FC<AuthLayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen bg-background text-foreground">
      <Navbar />
      <main className="container mx-auto mt-4 p-4">
        <div className="bg-card text-card-foreground rounded-lg shadow-md p-6">
          {children}
        </div>
      </main>
    </div>
  );
};

export default AuthLayout;