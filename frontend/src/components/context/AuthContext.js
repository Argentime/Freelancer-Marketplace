import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [role, setRole] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        const storedRole = localStorage.getItem('role');
        if (token && storedRole) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            setUser({ token });
            setRole(storedRole);
        }
        setLoading(false);
    }, []);

    const login = async (username, password, asGuest = false) => {
        try {
            let response;
            if (asGuest) {
                // Вход как гость (администратор)
                response = await axios.post('http://localhost:8080/api/auth/login', { username: 'admin', password: 'admin' });
                setRole('ADMIN');
            } else {
                response = await axios.post('http://localhost:8080/api/auth/login', { username, password });
                setRole('FREELANCER');
            }
            const { token } = response.data;
            localStorage.setItem('token', token);
            localStorage.setItem('role', asGuest ? 'ADMIN' : 'FREELANCER');
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            setUser({ token });
        } catch (error) {
            console.error('Login error:', error);
            throw new Error('Failed to login');
        }
    };

    const register = async (username, password, role) => {
        try {
            const response = await axios.post('http://localhost:8080/api/auth/register', { username, password, role });
            const { token } = response.data;
            localStorage.setItem('token', token);
            localStorage.setItem('role', role);
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            setUser({ token });
            setRole(role);
        } catch (error) {
            console.error('Register error:', error);
            throw new Error('Failed to register');
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        delete axios.defaults.headers.common['Authorization'];
        setUser(null);
        setRole(null);
    };

    return (
        <AuthContext.Provider value={{ user, role, login, register, logout, loading }}>
            {children}
        </AuthContext.Provider>
    );
};