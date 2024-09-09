// src/components/auth/LoginForm.tsx
"use client";

import React, { useState } from 'react';
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { useAuth } from '@/hooks/useAuth';
import SignUpForm from './SignUpForm';

const LoginForm = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [isLogin, setIsLogin] = useState(true);
  const { login } = useAuth();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    try {
      await login(username, password);
    } catch (err: any) {
      console.error('Login error:', err);
      setError(err.response?.data?.message || 'Invalid username or password');
    }
  };

  const toggleForm = () => {
    setIsLogin(!isLogin);
    setError('');
  };

  if (!isLogin) {
    return (
      <div>
        <SignUpForm />
        <p className="mt-4 text-center">
          Already have an account?{' '}
          <Button variant="link" onClick={toggleForm}>
            Log in
          </Button>
        </p>
      </div>
    );
  }

  return (
    <div>
      <Card className="w-[350px]">
        <CardHeader>
          <CardTitle>Login</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit}>
            <div className="grid w-full items-center gap-4">
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="username"
                  placeholder="Username"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                />
              </div>
              <div className="flex flex-col space-y-1.5">
                <Input
                  id="password"
                  type="password"
                  placeholder="Password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>
              {error && <p className="text-red-500">{error}</p>}
              <Button type="submit">Sign In</Button>
            </div>
          </form>
        </CardContent>
      </Card>
      <p className="mt-4 text-center">
        Don&apos;t have an account?{' '}
        <Button variant="link" onClick={toggleForm}>
          Sign up
        </Button>
      </p>
    </div>
  );
};

export default LoginForm;