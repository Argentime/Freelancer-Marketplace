import React, { useState } from 'react';
import { Dialog, DialogTitle, DialogContent, DialogActions, TextField, Button } from '@mui/material';

const OrderForm = ({ open, handleClose, onSubmit }) => {
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState('');

    const handleSubmit = () => {
        onSubmit(description, price);
        setDescription('');
        setPrice('');
        handleClose();
    };

    return (
        <Dialog open={open} onClose={handleClose}>
            <DialogTitle>Add Order</DialogTitle>
            <DialogContent>
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