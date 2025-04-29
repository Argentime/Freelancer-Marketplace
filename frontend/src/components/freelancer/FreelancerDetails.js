import React, { useState, useEffect } from 'react';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    Tabs,
    Tab,
    Typography,
    Grid,
    Card,
    CardContent,
    Button,
    Box, CardActions
} from '@mui/material';
import axios from 'axios';
import OrderForm from '../OrderForm';
import SkillForm from '../SkillForm';

const FreelancerDetails = ({ open, handleClose, freelancer }) => {
    const [tabValue, setTabValue] = useState(0);
    const [openOrderForm, setOpenOrderForm] = useState(false);
    const [openSkillForm, setOpenSkillForm] = useState(false);
    const [orders, setOrders] = useState([]);
    const [skills, setSkills] = useState([]);
    const [error, setError] = useState(null);

    useEffect(() => {
        if (freelancer) {
            console.log('Freelancer data:', freelancer);
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
            <DialogTitle sx={{ fontWeight: 'bold', color: '#1a1a1a', pb: 1 }}>
                {freelancer.name}
            </DialogTitle>
            <DialogContent>
                <Box sx={{ mb: 3 }}>
                    <Typography variant="body1" sx={{ color: '#555', mb: 1 }}>
                        ID: {freelancer.id}
                    </Typography>
                    <Typography variant="body1" sx={{ color: '#555', mb: 1 }}>
                        Category: {freelancer.category}
                    </Typography>
                    <Typography variant="body1" sx={{ color: '#f5c518', mb: 1 }}>
                        Rating: ★ {freelancer.rating}
                    </Typography>
                    <Typography variant="body1" sx={{ color: '#1dbf73', fontWeight: 'bold' }}>
                        Hourly Rate: ${freelancer.hourlyRate}
                    </Typography>
                </Box>
                {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}
                <Tabs
                    value={tabValue}
                    onChange={handleTabChange}
                    sx={{
                        mb: 2,
                        '& .MuiTab-root': { textTransform: 'none', fontWeight: 'bold' },
                        '& .Mui-selected': { color: '#1dbf73' },
                        '& .MuiTabs-indicator': { backgroundColor: '#1dbf73' }
                    }}
                >
                    <Tab label="Orders" />
                    <Tab label="Skills" />
                </Tabs>
                {tabValue === 0 && (
                    <Box sx={{ mt: 2 }}>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={() => setOpenOrderForm(true)}
                            sx={{ mb: 2, borderRadius: '20px', textTransform: 'none', px: 3 }}
                        >
                            Add Order
                        </Button>
                        <Grid container spacing={2}>
                            {orders && orders.length > 0 ? (
                                orders.map((order) => (
                                    <Grid item xs={12} sm={6} key={order.id}>
                                        <Card sx={{ borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
                                            <CardContent>
                                                <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                                                    Order {order.id}
                                                </Typography>
                                                <Typography variant="body2" color="text.secondary">
                                                    {order.description}
                                                </Typography>
                                                <Typography variant="body2" sx={{ color: '#1dbf73', mt: 1 }}>
                                                    ${order.price}
                                                </Typography>
                                            </CardContent>
                                            <CardActions sx={{ justifyContent: 'flex-end' }}>
                                                <Button
                                                    size="small"
                                                    onClick={() => handleDeleteOrder(order.id)}
                                                    sx={{ color: '#d32f2f', textTransform: 'none' }}
                                                >
                                                    Delete
                                                </Button>
                                            </CardActions>
                                        </Card>
                                    </Grid>
                                ))
                            ) : (
                                <Typography>No orders</Typography>
                            )}
                        </Grid>
                    </Box>
                )}
                {tabValue === 1 && (
                    <Box sx={{ mt: 2 }}>
                        <Button
                            variant="contained"
                            color="primary"
                            onClick={() => setOpenSkillForm(true)}
                            sx={{ mb: 2, borderRadius: '20px', textTransform: 'none', px: 3 }}
                        >
                            Add Skill
                        </Button>
                        <Grid container spacing={2}>
                            {skills && skills.length > 0 ? (
                                skills.map((skill) => (
                                    <Grid item xs={12} sm={4} key={skill.id}>
                                        <Card sx={{ borderRadius: '8px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
                                            <CardContent>
                                                <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                                                    {skill.name}
                                                </Typography>
                                            </CardContent>
                                            <CardActions sx={{ justifyContent: 'flex-end' }}>
                                                <Button
                                                    size="small"
                                                    onClick={() => handleDeleteSkill(skill.id)}
                                                    sx={{ color: '#d32f2f', textTransform: 'none' }}
                                                >
                                                    Delete
                                                </Button>
                                            </CardActions>
                                        </Card>
                                    </Grid>
                                ))
                            ) : (
                                <Typography>No skills</Typography>
                            )}
                        </Grid>
                    </Box>
                )}
            </DialogContent>
            <OrderForm open={openOrderForm} handleClose={() => setOpenOrderForm(false)} onSubmit={handleAddOrder} />
            <SkillForm open={openSkillForm} handleClose={() => setOpenSkillForm(false)} onSubmit={handleAddSkill} />
        </Dialog>
    );
};

export default FreelancerDetails;