import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button, Typography } from '@mui/material';

const SkillForm = ({ open, handleClose, onSubmit }) => {
    const [skillName, setSkillName] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = () => {
        if (!skillName.trim()) {
            setError('Skill name is required.');
            return;
        }
        setError(null);
        onSubmit(skillName);
        setSkillName('');
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Add Skill</DialogTitle>
            <DialogContent>
                {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}
                <TextField
                    margin="dense"
                    label="Skill Name"
                    value={skillName}
                    onChange={(e) => setSkillName(e.target.value)}
                    fullWidth
                    required
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleSubmit} color="primary">Add</Button>
            </DialogActions>
        </Dialog>
    );
};

export default SkillForm;