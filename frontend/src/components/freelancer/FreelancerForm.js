import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button, Typography } from '@mui/material';
import axios from 'axios';

const FreelancerForm = ({ open, handleClose, freelancer }) => {
    const [formData, setFormData] = useState({
        name: '',
        category: '',
        rating: '',
        hourlyRate: ''
    });
    const [error, setError] = useState(null);

    useEffect(() => {
        if (freelancer) {
            setFormData({
                name: freelancer.name || '',
                category: freelancer.category || '',
                rating: freelancer.rating || '',
                hourlyRate: freelancer.hourlyRate || ''
            });
        }
    }, [freelancer]);

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async () => {
        setError(null);
        try {
            const data = {
                name: formData.name,
                category: formData.category,
                rating: parseFloat(formData.rating),
                hourlyRate: parseFloat(formData.hourlyRate)
            };
            if (freelancer) {
                await axios.put(`http://localhost:8080/api/freelancers/${freelancer.id}`, data, {
                    headers: { 'Content-Type': 'application/json' }
                });
            } else {
                await axios.post('http://localhost:8080/api/freelancers', data, {
                    headers: { 'Content-Type': 'application/json' }
                });
            }
            handleClose();
        } catch (error) {
            console.error('Error saving freelancer:', error);
            setError('Failed to save freelancer. Please check the input.');
        }
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>{freelancer ? 'Edit Freelancer' : 'Add Freelancer'}</DialogTitle>
            <DialogContent>
                {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}
                <TextField
                    margin="dense"
                    name="name"
                    label="Name"
                    value={formData.name}
                    onChange={handleChange}
                    fullWidth
                    required
                />
                <TextField
                    margin="dense"
                    name="category"
                    label="Category"
                    value={formData.category}
                    onChange={handleChange}
                    fullWidth
                    required
                />
                <TextField
                    margin="dense"
                    name="rating"
                    label="Rating"
                    type="number"
                    value={formData.rating}
                    onChange={handleChange}
                    fullWidth
                    required
                    inputProps={{ step: '0.1', min: '0', max: '5' }}
                />
                <TextField
                    margin="dense"
                    name="hourlyRate"
                    label="Hourly Rate"
                    type="number"
                    value={formData.hourlyRate}
                    onChange={handleChange}
                    fullWidth
                    required
                    inputProps={{ step: '0.01', min: '0' }}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleSubmit} color="primary">Save</Button>
            </DialogActions>
        </Dialog>
    );
};

export default FreelancerForm;