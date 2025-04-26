import React from 'react';
import { Container, Typography, CssBaseline } from '@mui/material';
import FreelancerList from './components/FreelancerList';

function App() {
    return (
        <div>
            <CssBaseline />
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <Typography variant="h4" gutterBottom>
                    Freelancer Management
                </Typography>
                <FreelancerList />
            </Container>
        </div>
    );
}

export default App;
