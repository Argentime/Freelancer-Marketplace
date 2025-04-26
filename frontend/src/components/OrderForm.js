import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button, Typography } from '@mui/material';

const OrderForm = ({ open, handleClose, onSubmit }) => {
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = () => {
        if (!description || !price || isNaN(price) || price <= 0) {
            setError('Please provide a valid description and price.');
            return;
        }
        setError(null);
        onSubmit(description, parseFloat(price));
        setDescription('');
        setPrice('');
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Add Order</DialogTitle>
            <DialogContent>
                {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}
                <TextField
                    margin="dense"
                    label="Description"
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    fullWidth
                    required
                />
                <TextField
                    margin="dense"
                    label="Price"
                    type="number"
                    value={price}
                    onChange={(e) => setPrice(e.target.value)}
                    fullWidth
                    required
                    inputProps={{ step: '0.01', min: '0' }}
                />
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleSubmit} color="primary">Add</Button>
            </DialogActions>
        </Dialog>
    );
};

export default OrderForm;