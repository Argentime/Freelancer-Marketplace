import React from 'react';
import { Container, Typography, CssBaseline, AppBar, Toolbar } from '@mui/material';
import FreelancerList from './components/FreelancerList';

function App() {
    return (
        <div>
            <CssBaseline />
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6">Freelancer Management System</Typography>
                </Toolbar>
            </AppBar>
            <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
                <FreelancerList />
            </Container>
        </div>
    );
}

export default App;