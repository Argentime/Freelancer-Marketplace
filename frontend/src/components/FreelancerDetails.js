import React, { useState, useEffect } from 'react';
import { Dialog, DialogTitle, DialogContent, Tabs, Tab, Typography, List, ListItem, ListItemText, Button, Box } from '@mui/material';
import axios from 'axios';
import OrderForm from './OrderForm';
import SkillForm from './SkillForm';

const FreelancerDetails = ({ open, handleClose, freelancer }) => {
    const [tabValue, setTabValue] = useState(0);
    const [openOrderForm, setOpenOrderForm] = useState(false);
    const [openSkillForm, setOpenSkillForm] = useState(false);
    const [orders, setOrders] = useState([]);
    const [skills, setSkills] = useState([]);

    // Обновляем orders и skills при изменении freelancer
    useEffect(() => {
        if (freelancer) {
            console.log('Freelancer data:', freelancer); // Отладочный вывод
            setOrders(freelancer.orders || []);
            setSkills(freelancer.skills || []);
        }
    }, [freelancer]);

    if (!freelancer) return null;

    const handleTabChange = (event, newValue) => {
        setTabValue(newValue);
    };

    const handleAddOrder = async (description, price) => {
        try {
            const response = await axios.post(`http://localhost:8080/api/freelancers/${freelancer.id}/orders`, null, {
                params: { description, price }
            });
            setOrders(response.data.orders || []);
        } catch (error) {
            console.error('Error adding order:', error);
        }
    };

    const handleAddSkill = async (skillName) => {
        try {
            const response = await axios.post(`http://localhost:8080/api/freelancers/${freelancer.id}/skills`, null, {
                params: { skillName }
            });
            setSkills(response.data.skills || []);
        } catch (error) {
            console.error('Error adding skill:', error);
        }
    };

    const handleDeleteOrder = async (orderId) => {
        try {
            await axios.delete(`http://localhost:8080/api/freelancers/${freelancer.id}/orders/${orderId}`);
            setOrders(orders.filter(order => order.id !== orderId));
        } catch (error) {
            console.error('Error deleting order:', error);
        }
    };

    const handleDeleteSkill = async (skillId) => {
        try {
            await axios.delete(`http://localhost:8080/api/freelancers/${freelancer.id}/skills/${skillId}`);
            setSkills(skills.filter(skill => skill.id !== skillId));
        } catch (error) {
            console.error('Error deleting skill:', error);
        }
    };

    return (
        <Dialog open={open} onClose={handleClose} maxWidth="md" fullWidth>
            <DialogTitle>{freelancer.name} Details</DialogTitle>
            <DialogContent>
                <Typography variant="h6">ID: {freelancer.id}</Typography>
                <Typography>Category: {freelancer.category}</Typography>
                <Typography>Rating: {freelancer.rating}</Typography>
                <Typography>Hourly Rate: {freelancer.hourlyRate}</Typography>
                <Tabs value={tabValue} onChange={handleTabChange} sx={{ mt: 2 }}>
                    <Tab label="Orders" />
                    <Tab label="Skills" />
                </Tabs>
                {tabValue === 0 && (
                    <Box>
                        <Button variant="contained" color="primary" onClick={() => setOpenOrderForm(true)} sx={{ mb: 2 }}>
                            Add Order
                        </Button>
                        <List>
                            {orders && orders.length > 0 ? (
                                orders.map((order) => (
                                    <ListItem key={order.id}>
                                        <ListItemText primary={`Order ${order.id}: ${order.description} ($${order.price})`} />
                                        <Button color="error" onClick={() => handleDeleteOrder(order.id)}>Delete</Button>
                                    </ListItem>
                                ))
                            ) : (
                                <Typography>No orders</Typography>
                            )}
                        </List>
                    </Box>
                )}
                {tabValue === 1 && (
                    <Box>
                        <Button variant="contained" color="primary" onClick={() => setOpenSkillForm(true)} sx={{ mb: 2 }}>
                            Add Skill
                        </Button>
                        <List>
                            {skills && skills.length > 0 ? (
                                skills.map((skill) => (
                                    <ListItem key={skill.id}>
                                        <ListItemText primary={skill.name} />
                                        <Button color="error" onClick={() => handleDeleteSkill(skill.id)}>Delete</Button>
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