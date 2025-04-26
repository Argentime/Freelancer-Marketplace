import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button } from '@mui/material';

const SkillForm = ({ open, handleClose, onSubmit }) => {
    const [skillName, setSkillName] = useState('');

    const handleSubmit = () => {
        onSubmit(skillName);
        setSkillName('');
        handleClose();
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Add Skill</DialogTitle>
            <DialogContent>
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