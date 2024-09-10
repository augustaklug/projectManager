"use client";

import React from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/hooks/useAuth';
import { Button } from "@/components/ui/button";
import { ModeToggle } from "@/components/mode-toggle";
import { ThemeColorToggle } from "@/components/theme-color-toggle";
import { Home, LayoutDashboard, FolderKanban, CheckSquare, User, LogOut } from 'lucide-react';

const Navbar = () => {
  const { logout } = useAuth();
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  return (
    <nav className="bg-card text-card-foreground shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex">
            <Link href="/dashboard" className="flex-shrink-0 flex items-center">
              <Home className="h-8 w-8 text-primary" />
              <span className="ml-2 text-xl font-bold hidden sm:block">Project Manager</span>
            </Link>
            <div className="hidden sm:ml-6 sm:flex sm:space-x-8">
              <NavLink href="/dashboard" icon={<LayoutDashboard className="h-5 w-5" />}>Dashboard</NavLink>
              <NavLink href="/projects" icon={<FolderKanban className="h-5 w-5" />}>Projects</NavLink>
              <NavLink href="/tasks" icon={<CheckSquare className="h-5 w-5" />}>Tasks</NavLink>
            </div>
          </div>
          <div className="hidden sm:ml-6 sm:flex sm:items-center sm:space-x-4">
            <NavLink href="/profile" icon={<User className="h-5 w-5" />}>Profile</NavLink>
            <ModeToggle />
            <ThemeColorToggle />
            <Button onClick={handleLogout} variant="outline" size="sm" className="flex items-center">
              <LogOut className="h-4 w-4 mr-2" />
              Logout
            </Button>
          </div>
        </div>
      </div>
    </nav>
  );
};

const NavLink: React.FC<{ href: string; icon: React.ReactNode; children: React.ReactNode }> = ({ href, icon, children }) => (
  <Link href={href} className="inline-flex items-center px-1 pt-1 text-sm font-medium border-b-2 border-transparent hover:border-primary hover:text-primary transition-colors duration-200 ease-in-out">
    {icon}
    <span className="ml-2">{children}</span>
  </Link>
);

export default Navbar;