import React from 'react';
import Link from 'next/link';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen flex flex-col">
      <header className="bg-gray-800 text-white p-4">
        <nav className="container mx-auto flex justify-between items-center">
          <Link href="/" className="text-2xl font-bold">
            Project Manager
          </Link>
          <ul className="flex space-x-4">
            <li><Link href="/dashboard">Dashboard</Link></li>
            <li><Link href="/projects">Projects</Link></li>
            <li><Link href="/tasks">Tasks</Link></li>
            <li><Link href="/profile">Profile</Link></li>
          </ul>
        </nav>
      </header>
      <main className="flex-grow container mx-auto p-4">
        {children}
      </main>
      <footer className="bg-gray-800 text-white p-4">
        <div className="container mx-auto text-center">
          © 2024 Project Manager. All rights reserved.
        </div>
      </footer>
    </div>
  );
};

export default Layout;