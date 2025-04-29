import React, { useContext } from 'react';
import { Typography, Button, Box } from '@mui/material';
import { RoleContext } from './context/RoleContext';

const RoleSelector = () => {
    const { selectRole } = useContext(RoleContext);

    return (
        <Box sx={{ maxWidth: 400, mx: 'auto', mt: 4, textAlign: 'center' }}>
            <Typography variant="h5" gutterBottom>
                Select Your Role
            </Typography>
            <Button
                variant="contained"
                color="primary"
                onClick={() => selectRole('FREELANCER')}
                sx={{ m: 1 }}
                fullWidth
            >
                Freelancer
            </Button>
            <Button
                variant="contained"
                color="secondary"
                onClick={() => selectRole('ADMIN')}
                sx={{ m: 1 }}
                fullWidth
            >
                Admin (Guest)
            </Button>
        </Box>
    );
};

export default RoleSelector;