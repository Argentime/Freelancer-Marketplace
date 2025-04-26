import React, { useState, useEffect } from 'react';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, Box, Typography } from '@mui/material';
import axios from 'axios';
import FreelancerForm from './FreelancerForm';
import FreelancerDetails from './FreelancerDetails';

const FreelancerList = () => {
    const [freelancers, setFreelancers] = useState([]);
    const [openForm, setOpenForm] = useState(false);
    const [openDetails, setOpenDetails] = useState(false);
    const [selectedFreelancer, setSelectedFreelancer] = useState(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        fetchFreelancers();
    }, []);

    const fetchFreelancers = async () => {
        setLoading(true);
        try {
            const response = await axios.get('http://localhost:8080/api/freelancers');
            setFreelancers(response.data);
        } catch (error) {
            console.error('Error fetching freelancers:', error);
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

    return (
        <Box>
            <Button variant="contained" color="primary" onClick={handleAdd} sx={{ mb: 2 }} disabled={loading}>
                Add Freelancer
            </Button>
            {loading ? (
                <Typography>Loading...</Typography>
            ) : (
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>ID</TableCell>
                                <TableCell>Name</TableCell>
                                <TableCell>Category</TableCell>
                                <TableCell>Rating</TableCell>
                                <TableCell>Hourly Rate</TableCell>
                                <TableCell>Actions</TableCell>
                            </TableRow>
                        </TableHead>
                        <TableBody>
                            {freelancers.map((freelancer) => (
                                <TableRow key={freelancer.id}>
                                    <TableCell>{freelancer.id}</TableCell>
                                    <TableCell>{freelancer.name}</TableCell>
                                    <TableCell>{freelancer.category}</TableCell>
                                    <TableCell>{freelancer.rating}</TableCell>
                                    <TableCell>{freelancer.hourlyRate}</TableCell>
                                    <TableCell>
                                        <Button onClick={() => handleViewDetails(freelancer)}>View</Button>
                                        <Button onClick={() => handleEdit(freelancer)} color="primary">Edit</Button>
                                        <Button onClick={() => handleDelete(freelancer.id)} color="error">Delete</Button>
                                    </TableCell>
                                </TableRow>
                            ))}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}
            <FreelancerForm open={openForm} handleClose={handleFormClose} freelancer={selectedFreelancer} />
            <FreelancerDetails open={openDetails} handleClose={() => setOpenDetails(false)} freelancer={selectedFreelancer} />
        </Box>
    );
};

export default FreelancerList;