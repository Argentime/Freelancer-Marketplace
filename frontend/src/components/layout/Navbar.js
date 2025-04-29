import React from 'react';
import { AppBar, Toolbar, Typography, Button, Dialog, DialogTitle, DialogContent, DialogActions } from '@mui/material';

const Navbar = () => {
    const [openAuthDialog, setOpenAuthDialog] = React.useState(false);

    const handleAuthClick = () => {
        setOpenAuthDialog(true);
    };

    return (
        <>
            <AppBar position="static">
                <Toolbar>
                    <Typography variant="h6" sx={{ flexGrow: 1 }}>
                        Freelance Marketplace
                    </Typography>
                    <Typography sx={{ mr: 2 }}>Guest (Admin)</Typography>
                    <Button color="inherit" onClick={handleAuthClick}>
                        Login/Register
                    </Button>
                </Toolbar>
            </AppBar>
            <Dialog open={openAuthDialog} onClose={() => setOpenAuthDialog(false)}>
                <DialogTitle>Authentication</DialogTitle>
                <DialogContent>
                    <Typography>Login and registration are not yet implemented. You are currently logged in as Guest (Admin).</Typography>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setOpenAuthDialog(false)}>Close</Button>
                </DialogActions>
            </Dialog>
        </>
    );
};

export default Navbar;