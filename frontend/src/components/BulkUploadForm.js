import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button, Typography } from '@mui/material';
import axios from 'axios';

const BulkUploadForm = ({ open, handleClose }) => {
    const [jsonInput, setJsonInput] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = async () => {
        setError(null);
        try {
            const parsedData = JSON.parse(jsonInput);
            if (!Array.isArray(parsedData)) {
                throw new Error('Input must be a JSON array.');
            }
            await axios.post('http://localhost:8080/api/freelancers/bulk', parsedData, {
                headers: { 'Content-Type': 'application/json' }
            });
            handleClose();
        } catch (error) {
            console.error('Error uploading bulk freelancers:', error);
            setError('Failed to upload. Please check the JSON format.');
        }
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Bulk Upload Freelancers</DialogTitle>
            <DialogContent>
                <Typography sx={{ mb: 2 }}>
                    Enter a JSON array of freelancers, e.g.:
                </Typography>
                {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}
                <TextField
                    margin="dense"
                    label="JSON Input"
                    multiline
                    rows={6}
                    value={jsonInput}
                    onChange={(e) => setJsonInput(e.target.value)}
                    fullWidth
                    required
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleSubmit} color="primary">Upload</Button>
            </DialogActions>
        </Dialog>
    );
};

export default BulkUploadForm;