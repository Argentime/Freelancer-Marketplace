import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button } from '@mui/material';
import axios from 'axios';

const FreelancerForm = ({ open, handleClose, freelancer }) => {
    const [formData, setFormData] = useState({
        name: '',
        category: '',
        rating: '',
        hourlyRate: ''
    });

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
        try {
            if (freelancer) {
                await axios.put(`http://localhost:8080/api/freelancers/${freelancer.id}`, formData);
            } else {
                await axios.post('http://localhost:8080/api/freelancers', formData);
            }
            handleClose();
        } catch (error) {
            console.error('Error saving freelancer:', error);
        }
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>{freelancer ? 'Edit Freelancer' : 'Add Freelancer'}</DialogTitle>
            <DialogContent>
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