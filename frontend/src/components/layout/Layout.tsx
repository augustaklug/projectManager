import React from 'react';
import Link from 'next/link';
import { ModeToggle } from "@/components/mode-toggle";
import { ThemeColorToggle } from "@/components/theme-color-toggle";
import { Home, LayoutDashboard, FolderKanban, CheckSquare, User, Menu } from 'lucide-react';
import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetContent,
  SheetTrigger,
} from "@/components/ui/sheet"

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
            <span className="hidden sm:inline">Project Manager</span>
          </Link>
          <div className="flex items-center space-x-4">
            <ul className="hidden md:flex items-center space-x-4">
              <NavLink href="/dashboard" icon={<LayoutDashboard className="h-4 w-4" />} text="Dashboard" />
              <NavLink href="/projects" icon={<FolderKanban className="h-4 w-4" />} text="Projects" />
              <NavLink href="/tasks" icon={<CheckSquare className="h-4 w-4" />} text="Tasks" />
              <NavLink href="/profile" icon={<User className="h-4 w-4" />} text="Profile" />
            </ul>
            <div className="flex items-center space-x-2">
              <ModeToggle />
              <ThemeColorToggle />
            </div>
            <Sheet>
              <SheetTrigger asChild>
                <Button variant="outline" size="icon" className="md:hidden">
                  <Menu className="h-4 w-4" />
                  <span className="sr-only">Toggle menu</span>
                </Button>
              </SheetTrigger>
              <SheetContent side="right">
                <nav className="flex flex-col space-y-4 mt-4">
                  <NavLink href="/dashboard" icon={<LayoutDashboard className="h-4 w-4" />} text="Dashboard" />
                  <NavLink href="/projects" icon={<FolderKanban className="h-4 w-4" />} text="Projects" />
                  <NavLink href="/tasks" icon={<CheckSquare className="h-4 w-4" />} text="Tasks" />
                  <NavLink href="/profile" icon={<User className="h-4 w-4" />} text="Profile" />
                </nav>
              </SheetContent>
            </Sheet>
          </div>
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

const NavLink: React.FC<{ href: string; icon: React.ReactNode; text: string }> = ({ href, icon, text }) => (
  <li>
    <Link href={href} className="flex items-center hover:text-primary transition-colors">
      {icon}
      <span className="ml-2">{text}</span>
    </Link>
  </li>
);

export default Layout;