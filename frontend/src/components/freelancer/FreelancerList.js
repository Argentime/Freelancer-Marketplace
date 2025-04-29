import React, { useState, useEffect } from 'react';
import {
    Box,
    Typography,
    Grid,
    Card,
    CardContent,
    Button,
    CardActions
} from '@mui/material';
import axios from 'axios';
import FreelancerForm from './FreelancerForm';
import FreelancerDetails from './FreelancerDetails';
import BulkUploadForm from '../BulkUploadForm';
import FilterForm from '../FilterForm';

const FreelancerList = () => {
    const [freelancers, setFreelancers] = useState([]);
    const [openForm, setOpenForm] = useState(false);
    const [openDetails, setOpenDetails] = useState(false);
    const [openBulkForm, setOpenBulkForm] = useState(false);
    const [selectedFreelancer, setSelectedFreelancer] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [filters, setFilters] = useState({ category: '', skillName: '' });

    useEffect(() => {
        fetchFreelancers();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [filters]);

    const fetchFreelancers = async () => {
        setLoading(true);
        setError(null);
        try {
            const hasFilters = filters.category.trim() || filters.skillName.trim();
            let response;
            if (hasFilters) {
                const params = {};
                if (filters.category.trim()) params.category = filters.category.trim();
                if (filters.skillName.trim()) params.skillName = filters.skillName.trim();
                response = await axios.get('http://localhost:8080/api/freelancers', { params });
            } else {
                response = await axios.get('http://localhost:8080/api/freelancers');
            }
            console.log('Freelancers response:', response.data);
            setFreelancers(response.data);
        } catch (error) {
            console.error('Error fetching freelancers:', {
                message: error.message,
                response: error.response ? {
                    status: error.response.status,
                    data: error.response.data
                } : null,
                config: error.config
            });
            setError(`Failed to fetch freelancers: ${error.message}${error.response ? ` (Status: ${error.response.status})` : ''}`);
        } finally {
            setLoading(false);
        }
    };

    const handleAdd = () => {
        setSelectedFreelancer(null);
        setOpenForm(true);
    };

    const handleEdit = (freelancer) => {
        setSelectedFreelancer(freelancer);
        setOpenForm(true);
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/api/freelancers/${id}`);
            fetchFreelancers();
        } catch (error) {
            console.error('Error deleting freelancer:', error);
            setError('Failed to delete freelancer.');
        }
    };

    const handleViewDetails = (freelancer) => {
        setSelectedFreelancer(freelancer);
        setOpenDetails(true);
    };

    const handleFormClose = () => {
        setOpenForm(false);
        fetchFreelancers();
    };

    const handleBulkFormClose = () => {
        setOpenBulkForm(false);
        fetchFreelancers();
    };

    const handleFilterApply = (newFilters) => {
        setFilters(newFilters);
    };

    return (
        <Box>
            <Typography variant="h4" gutterBottom sx={{ fontWeight: 'bold', color: '#1a1a1a' }}>
                Freelancers
            </Typography>
            <Grid container spacing={2} sx={{ mb: 3 }}>
                <Grid item>
                    <Button
                        variant="contained"
                        color="primary"
                        onClick={handleAdd}
                        disabled={loading}
                        sx={{ borderRadius: '20px', textTransform: 'none', px: 3 }}
                    >
                        Add Freelancer
                    </Button>
                </Grid>
                <Grid item>
                    <Button
                        variant="contained"
                        color="secondary"
                        onClick={() => setOpenBulkForm(true)}
                        disabled={loading}
                        sx={{ borderRadius: '20px', textTransform: 'none', px: 3 }}
                    >
                        Bulk Upload
                    </Button>
                </Grid>
            </Grid>
            <FilterForm onApply={handleFilterApply} />
            {error && <Typography color="error" sx={{ mb: 2 }}>{error}</Typography>}
            {loading ? (
                <Typography>Loading...</Typography>
            ) : (
                <Grid container spacing={3}>
                    {freelancers.map((freelancer) => (
                        <Grid item xs={12} sm={6} md={4} key={freelancer.id}>
                            <Card
                                sx={{
                                    borderRadius: '10px',
                                    boxShadow: '0 4px 12px rgba(0,0,0,0.1)',
                                    transition: 'transform 0.2s',
                                    '&:hover': { transform: 'scale(1.02)' }
                                }}
                            >
                                <CardContent>
                                    <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#1a1a1a' }}>
                                        {freelancer.name}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                                        {freelancer.category}
                                    </Typography>
                                    <Typography variant="body2" sx={{ color: '#f5c518', mb: 1 }}>
                                        ★ {freelancer.rating}
                                    </Typography>
                                    <Typography variant="h6" sx={{ color: '#1dbf73', fontWeight: 'bold' }}>
                                        From ${freelancer.hourlyRate}/hr
                                    </Typography>
                                </CardContent>
                                <CardActions sx={{ justifyContent: 'flex-end', p: 2 }}>
                                    <Button
                                        size="small"
                                        onClick={() => handleViewDetails(freelancer)}
                                        sx={{ color: '#1a1a1a', textTransform: 'none' }}
                                    >
                                        View
                                    </Button>
                                    <Button
                                        size="small"
                                        onClick={() => handleEdit(freelancer)}
                                        sx={{ color: '#1976d2', textTransform: 'none' }}
                                    >
                                        Edit
                                    </Button>
                                    <Button
                                        size="small"
                                        onClick={() => handleDelete(freelancer.id)}
                                        sx={{ color: '#d32f2f', textTransform: 'none' }}
                                    >
                                        Delete
                                    </Button>
                                </CardActions>
                            </Card>
                        </Grid>
                    ))}
                </Grid>
            )}
            <FreelancerForm open={openForm} handleClose={handleFormClose} freelancer={selectedFreelancer} />
            <FreelancerDetails open={openDetails} handleClose={() => setOpenDetails(false)} freelancer={selectedFreelancer} />
            <BulkUploadForm open={openBulkForm} handleClose={handleBulkFormClose} />
        </Box>
    );
};

export default FreelancerList;