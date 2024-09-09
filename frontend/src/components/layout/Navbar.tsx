// src/components/layout/Navbar.tsx
"use client";

import React from 'react';
import Link from 'next/link';
import { useRouter } from 'next/navigation';
import { useAuth } from '@/hooks/useAuth';
import { Button } from "@/components/ui/button";
import { ModeToggle } from "@/components/mode-toggle";
import { ThemeColorToggle } from "@/components/theme-color-toggle";

const Navbar = () => {
  const { logout } = useAuth();
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  return (
    <nav className="bg-background border-b">
      <div className="max-w-6xl mx-auto px-4">
        <div className="flex justify-between">
          <div className="flex space-x-7">
            <div>
              <Link href="/dashboard" className="flex items-center py-4 px-2">
                <span className="font-semibold text-foreground text-lg">Project Manager</span>
              </Link>
            </div>
            <div className="hidden md:flex items-center space-x-1">
              <Link href="/dashboard" className="py-4 px-2 text-foreground font-semibold hover:text-primary transition duration-300">Dashboard</Link>
              <Link href="/projects" className="py-4 px-2 text-foreground font-semibold hover:text-primary transition duration-300">Projects</Link>
              <Link href="/tasks" className="py-4 px-2 text-foreground font-semibold hover:text-primary transition duration-300">Tasks</Link>
            </div>
          </div>
          <div className="hidden md:flex items-center space-x-3">
            <Link href="/profile" className="py-2 px-2 font-medium text-foreground rounded hover:bg-primary hover:text-primary-foreground transition duration-300">Profile</Link>
            <ModeToggle />
            <ThemeColorToggle />
            <Button onClick={handleLogout} variant="outline">Logout</Button>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;