import React, { useState, useContext } from 'react';
import { TextField, Button, Typography, Box, Checkbox, FormControlLabel } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [isGuest, setIsGuest] = useState(false);
    const [error, setError] = useState(null);
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    const handleSubmit = async () => {
        setError(null);
        try {
            await login(username, password, isGuest);
            navigate('/');
        } catch (error) {
            setError(error.message);
        }
    };

    return (
        <Box sx={{ maxWidth: 400, mx: 'auto', mt: 4 }}>
            <Typography variant="h5" gutterBottom>
                Login
            </Typography>
            {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}
            <TextField
                label="Username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                fullWidth
                margin="normal"
                disabled={isGuest}
            />
            <TextField
                label="Password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                fullWidth
                margin="normal"
                disabled={isGuest}
            />
            <FormControlLabel
                control={<Checkbox checked={isGuest} onChange={(e) => setIsGuest(e.target.checked)} />}
                label="Login as Guest (Admin)"
            />
            <Button variant="contained" onClick={handleSubmit} fullWidth sx={{ mt: 2 }}>
                Login
            </Button>
        </Box>
    );
};

export default Login;