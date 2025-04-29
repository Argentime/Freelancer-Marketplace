import React from 'react';
import { CssBaseline, Container } from '@mui/material';
import Navbar from './components/layout/Navbar';
import FreelancerList from './components/freelancer/FreelancerList';

function App() {
    return (
        <div>
            <CssBaseline />
            <Navbar />
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <FreelancerList />
            </Container>
        </div>
    );
}

export default App;