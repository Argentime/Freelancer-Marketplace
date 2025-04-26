import React, { useState, useEffect } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    Tabs,
    Tab,
    Typography,
    List,
    ListItem,
    ListItemText,
    Button,
    Box
} from '@mui/material';
import axios from 'axios';
import OrderForm from './OrderForm';
import SkillForm from './SkillForm';

const FreelancerDetails = ({ open, handleClose, freelancer }) => {
    const [tabValue, setTabValue] = useState(0);
    const [openOrderForm, setOpenOrderForm] = useState(false);
    const [openSkillForm, setOpenSkillForm] = useState(false);
    const [orders, setOrders] = useState([]);
    const [skills, setSkills] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (freelancer) {
            setOrders(freelancer.orders || []);
            setSkills(freelancer.skills || []);
        }
    }, [freelancer]);

    const handleTabChange = (event, newValue) => {
        setTabValue(newValue);
    };

    const handleAddOrder = async (description, price) => {
        try {
            const response = await axios.post(`http://localhost:8080/api/freelancers/${freelancer.id}/orders`, null, {
                params: { description, price }
            });
            setOrders(response.data.orders || []);
            setError(null);
        } catch (error) {
            console.error('Error adding order:', error);
            setError('Failed to add order.');
        }
    };

    const handleAddSkill = async (skillName) => {
        try {
            const response = await axios.post(`http://localhost:8080/api/freelancers/${freelancer.id}/skills`, null, {
                params: { skillName }
            });
            setSkills(response.data.skills || []);
            setError(null);
        } catch (error) {
            console.error('Error adding skill:', error);
            setError('Failed to add skill.');
        }
    };

    const handleDeleteOrder = async (orderId) => {
        try {
            await axios.delete(`http://localhost:8080/api/freelancers/${freelancer.id}/orders/${orderId}`);
            setOrders(orders.filter((order) => order.id !== orderId));
            setError(null);
        } catch (error) {
            console.error('Error deleting order:', error);
            setError('Failed to delete order.');
        }
    };

    const handleDeleteSkill = async (skillId) => {
        try {
            await axios.delete(`http://localhost:8080/api/freelancers/${freelancer.id}/skills/${skillId}`);
            setSkills(skills.filter((skill) => skill.id !== skillId));
            setError(null);
        } catch (error) {
            console.error('Error deleting skill:', error);
            setError('Failed to delete skill.');
        }
    };

    if (!freelancer) return null;

    return (
        <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
            <DialogTitle>{freelancer.name} Details</DialogTitle>
            <DialogContent>
                <Typography variant="h6">ID: {freelancer.id}</Typography>
                <Typography>Category: {freelancer.category}</Typography>
                <Typography>Rating: {freelancer.rating}</Typography>
                <Typography>Hourly Rate: ${freelancer.hourlyRate}</Typography>
                {error && <Typography color="error" sx={{ mt: 2 }}>{error}</Typography>}
                <Tabs value={tabValue} onChange={handleTabChange} sx={{ mt: 2 }}>
                    <Tab label="Orders" />
                    <Tab label="Skills" />
                </Tabs>
                {tabValue === 0 && (
                    <Box sx={{ mt: 2 }}>
                        <Button variant="contained" color="primary" onClick={() => setOpenOrderForm(true)} sx={{ mb: 2 }}>
                            Add Order
                        </Button>
                        <List>
                            {orders && orders.length > 0 ? (
                                orders.map((order) => (
                                    <ListItem key={order.id}>
                                        <ListItemText primary={`Order ${order.id}: ${order.description} ($${order.price})`} />
                                        <Button color="error" onClick={() => handleDeleteOrder(order.id)}>
                                            Delete
                                        </Button>
                                    </ListItem>
                                ))
                            ) : (
                                <Typography>No orders</Typography>
                            )}
                        </List>
                    </Box>
                )}
                {tabValue === 1 && (
                    <Box sx={{ mt: 2 }}>
                        <Button variant="contained" color="primary" onClick={() => setOpenSkillForm(true)} sx={{ mb: 2 }}>
                            Add Skill
                        </Button>
                        <List>
                            {skills && skills.length > 0 ? (
                                skills.map((skill) => (
                                    <ListItem key={skill.id}>
                                        <ListItemText primary={skill.name} />
                                        <Button color="error" onClick={() => handleDeleteSkill(skill.id)}>
                                            Delete
                                        </Button>
                                    </ListItem>
                                ))
                            ) : (
                                <Typography>No skills</Typography>
                            )}
                        </List>
                    </Box>
                )}
            </DialogContent>
            <OrderForm open={openOrderForm} handleClose={() => setOpenOrderForm(false)} onSubmit={handleAddOrder} />
            <SkillForm open={openSkillForm} handleClose={() => setOpenSkillForm(false)} onSubmit={handleAddSkill} />
        </Dialog>
    );
};

export default FreelancerDetails;