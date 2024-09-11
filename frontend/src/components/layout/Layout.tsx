import React from 'react';
import Navbar from '@/components/layout/Navbar';

interface LayoutProps {
    children: React.ReactNode;
}

const Layout: React.FC<LayoutProps> = ({children}) => {
    return (
        <div className="min-h-screen flex flex-col bg-background text-foreground">
            <Navbar/>
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