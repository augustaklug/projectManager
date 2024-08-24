import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { userService, UserData } from '@/services/userService';
import { useAuth } from '@/hooks/useAuth';
import { User, Mail, Briefcase } from 'lucide-react';

const Profile = () => {
    const [profile, setProfile] = useState<UserData | null>(null);
    const [error, setError] = useState('');
    const { logout } = useAuth();
    const router = useRouter();

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const data = await userService.getCurrentUser();
                console.log('Fetched profile:', data);
                setProfile(data);
            } catch (err) {
                console.error('Error fetching profile:', err);
                if (err instanceof Error) {
                    if (err.message === 'User not authenticated') {
                        setError('Your session has expired. Please log in again.');
                        logout();
                    } else {
                        setError(`Failed to fetch profile: ${err.message}`);
                    }
                } else {
                    setError('An unknown error occurred while fetching the profile.');
                }
            }
        };

        fetchProfile();
    }, [logout]);

    const getFriendlyRoleName = (role: string | undefined): string => {
        if (!role) return 'Not set';
        switch (role) {
            case 'ROLE_ADMIN':
                return 'Administrator';
            case 'ROLE_USER':
                return 'User';
            case 'ROLE_MANAGER':
                return 'Manager';
            default:
                return role.replace('ROLE_', '').toLowerCase().replace(/\b\w/g, l => l.toUpperCase());
        }
    };

    if (error) {
        return (
            <Card className="w-[600px] mx-auto">
                <CardContent className="pt-6">
                    <p className="text-red-500">{error}</p>
                    {error.includes('session has expired') && (
                        <Button onClick={() => router.push('/login')} className="mt-4">Log In Again</Button>
                    )}
                </CardContent>
            </Card>
        );
    }

    if (!profile) {
        return <div className="text-center">Loading profile...</div>;
    }

    return (
        <Card className="w-[600px] mx-auto">
            <CardHeader>
                <CardTitle className="text-2xl font-bold">User Profile</CardTitle>
            </CardHeader>
            <CardContent className="space-y-6">
                <div className="flex items-center space-x-4">
                    <User className="h-6 w-6 text-gray-400"/>
                    <div>
                        <p className="text-sm font-medium text-gray-500">Username</p>
                        <p className="text-lg font-semibold">{profile.username}</p>
                    </div>
                </div>
                <div className="flex items-center space-x-4">
                    <Mail className="h-6 w-6 text-gray-400"/>
                    <div>
                        <p className="text-sm font-medium text-gray-500">Email</p>
                        <p className="text-lg font-semibold">{profile.email || 'Not set'}</p>
                    </div>
                </div>
                <div className="flex items-center space-x-4">
                    <Briefcase className="h-6 w-6 text-gray-400"/>
                    <div>
                        <p className="text-sm font-medium text-gray-500">Role</p>
                        <p className="text-lg font-semibold">{getFriendlyRoleName(profile.role)}</p>
                    </div>
                </div>
            </CardContent>
        </Card>
    );
};

export default Profile;