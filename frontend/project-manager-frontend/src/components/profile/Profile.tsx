import React, {useState, useEffect} from 'react';
import {useRouter} from 'next/navigation';
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import {Input} from "@/components/ui/input";
import {userService, UserData} from '@/services/userService';
import {useAuth} from '@/hooks/useAuth';

const Profile = () => {
    const [profile, setProfile] = useState<UserData | null>(null);
    const [isEditing, setIsEditing] = useState(false);
    const [editedProfile, setEditedProfile] = useState<Partial<UserData>>({});
    const [error, setError] = useState('');
    const {logout} = useAuth();
    const router = useRouter();

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const data = await userService.getCurrentUser();
                setProfile(data);
                setEditedProfile(data);
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

    const handleEdit = () => {
        setIsEditing(true);
    };

    const handleSave = async () => {
        try {
            const updatedProfile = await userService.updateCurrentUser(editedProfile);
            setProfile(updatedProfile);
            setIsEditing(false);
            setError('');
        } catch (err) {
            console.error('Error updating profile:', err);
            if (err instanceof Error && err.message === 'Invalid user ID') {
                setError('Your session has expired. Please log in again.');
                logout();
            } else {
                setError('Failed to update profile. Please try again.');
            }
        }
    };

    const handleCancel = () => {
        setEditedProfile(profile || {});
        setIsEditing(false);
        setError('');
    };

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setEditedProfile({...editedProfile, [e.target.name]: e.target.value});
    };

    if (error) {
        return (
            <Card className="w-[600px] mx-auto">
                <CardContent>
                    <p className="text-red-500">{error}</p>
                    {error.includes('session has expired') && (
                        <Button onClick={() => router.push('/login')}>Log In Again</Button>
                    )}
                </CardContent>
            </Card>
        );
    }

    if (!profile) {
        return <div>Loading profile...</div>;
    }

    return (
        <Card className="w-[600px] mx-auto">
            <CardHeader>
                <CardTitle>User Profile</CardTitle>
            </CardHeader>
            <CardContent>
                {error && <p className="text-red-500 mb-4">{error}</p>}
                {profile ? (
                    <form>
                        {/* ... form fields ... */}
                    </form>
                ) : (
                    <p>Loading profile...</p>
                )}
            </CardContent>
        </Card>
    );
};

export default Profile;