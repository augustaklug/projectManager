import React, {useState, useEffect, useCallback} from 'react';
import {useRouter} from 'next/navigation';
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {userService, UserData} from '@/services/userService';
import {useAuth} from '@/hooks/useAuth';
import {User, Mail, Briefcase, Loader2} from 'lucide-react';
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar"
import {Alert, AlertDescription, AlertTitle} from "@/components/ui/alert"

export default function Profile() {
    const [profile, setProfile] = useState<UserData | null>(null);
    const [error, setError] = useState('');
    const [shouldFetchProfile, setShouldFetchProfile] = useState(true);
    const {logout} = useAuth();
    const router = useRouter();

    const fetchProfile = useCallback(async () => {
        if (!shouldFetchProfile) return;

        try {
            const data = await userService.getCurrentUser();
            console.log('Fetched profile:', data);
            setProfile(data);
            setShouldFetchProfile(false);
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
            setShouldFetchProfile(false);
        }
    }, [shouldFetchProfile, logout]);

    useEffect(() => {
        fetchProfile();
    }, [fetchProfile]);

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
            <div className="container mx-auto px-4 py-8">
                <Alert variant="destructive" className="max-w-md mx-auto">
                    <AlertTitle>Error</AlertTitle>
                    <AlertDescription>{error}</AlertDescription>
                    {error.includes('session has expired') && (
                        <Button onClick={() => router.push('/login')} className="mt-4">Log In Again</Button>
                    )}
                </Alert>
            </div>
        );
    }

    if (!profile) {
        return (
            <div className="flex justify-center items-center min-h-[50vh]">
                <Loader2 className="h-8 w-8 animate-spin text-primary"/>
            </div>
        );
    }

    return (
        <div className="container mx-auto px-4 py-8">
            <Card className="max-w-2xl mx-auto">
                <CardHeader className="pb-4">
                    <div className="flex flex-col sm:flex-row items-center space-y-4 sm:space-y-0 sm:space-x-4">
                        <Avatar className="h-24 w-24">
                            <AvatarImage src={'/placeholder.svg?height=96&width=96'} alt={profile.username}/>
                            <AvatarFallback>{profile.username?.charAt(0).toUpperCase()}</AvatarFallback>
                        </Avatar>
                        <div className="text-center sm:text-left">
                            <CardTitle className="text-2xl font-bold">{profile.username}</CardTitle>
                            <p className="text-sm text-muted-foreground">{getFriendlyRoleName(profile.role)}</p>
                        </div>
                    </div>
                </CardHeader>
                <CardContent className="space-y-6">
                    <div className="grid gap-4 sm:grid-cols-2">
                        <div className="flex items-center space-x-4">
                            <Mail className="h-5 w-5 text-muted-foreground"/>
                            <div>
                                <p className="text-sm font-medium text-muted-foreground">Email</p>
                                <p className="text-sm font-semibold">{profile.email || 'Not set'}</p>
                            </div>
                        </div>
                        <div className="flex items-center space-x-4">
                            <Briefcase className="h-5 w-5 text-muted-foreground"/>
                            <div>
                                <p className="text-sm font-medium text-muted-foreground">Role</p>
                                <p className="text-sm font-semibold">{getFriendlyRoleName(profile.role)}</p>
                            </div>
                        </div>
                    </div>
                </CardContent>
            </Card>
        </div>
    );
}