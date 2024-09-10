import React from 'react';
import Link from 'next/link';
import { ModeToggle } from "@/components/mode-toggle";
import { ThemeColorToggle } from "@/components/theme-color-toggle";
import { Home, LayoutDashboard, FolderKanban, CheckSquare, User } from 'lucide-react';

interface LayoutProps {
  children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <div className="min-h-screen flex flex-col bg-background text-foreground">
      <header className="bg-card text-card-foreground shadow-sm">
        <nav className="container mx-auto px-4 py-3 flex justify-between items-center">
          <Link href="/" className="text-2xl font-bold flex items-center">
            <Home className="mr-2 h-6 w-6" />
            Project Manager
          </Link>
          <ul className="flex items-center space-x-4">
            <li>
              <Link href="/dashboard" className="flex items-center hover:text-primary transition-colors">
                <LayoutDashboard className="mr-1 h-4 w-4" />
                <span className="hidden sm:inline">Dashboard</span>
              </Link>
            </li>
            <li>
              <Link href="/projects" className="flex items-center hover:text-primary transition-colors">
                <FolderKanban className="mr-1 h-4 w-4" />
                <span className="hidden sm:inline">Projects</span>
              </Link>
            </li>
            <li>
              <Link href="/tasks" className="flex items-center hover:text-primary transition-colors">
                <CheckSquare className="mr-1 h-4 w-4" />
                <span className="hidden sm:inline">Tasks</span>
              </Link>
            </li>
            <li>
              <Link href="/profile" className="flex items-center hover:text-primary transition-colors">
                <User className="mr-1 h-4 w-4" />
                <span className="hidden sm:inline">Profile</span>
              </Link>
            </li>
            <li className="flex items-center space-x-2">
              <ModeToggle />
              <ThemeColorToggle />
            </li>
          </ul>
        </nav>
      </header>
      <main className="flex-grow container mx-auto px-4 py-8">
        {children}
      </main>
      <footer className="bg-card text-card-foreground py-4">
        <div className="container mx-auto px-4 text-center text-sm">
          © {new Date().getFullYear()} Project Manager. All rights reserved.
        </div>
      </footer>
    </div>
  );
};

export default Layout;