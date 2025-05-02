import React, { useState } from 'react';
import { TextField, Button, Grid, FormControl } from '@mui/material';

const FilterForm = ({ onApply }) => {
    const [category, setCategory] = useState('');
    const [skillName, setSkillName] = useState('');

    const handleApply = () => {
        onApply({
            category: category.trim() || '',
            skillName: skillName.trim() || ''
        });
    };

    const handleClear = () => {
        setCategory('');
        setSkillName('');
        onApply({ category: '', skillName: '' });
    };

    return (
        <Grid container spacing={2} sx={{ mb: 2 }}>
                <Grid item xs={12} sm={5}>
                    <TextField
                        label="Category"
                        value={category}
                        onChange={(e) => setCategory(e.target.value)}
                        fullWidth
                        size = 'small'
                        placeholder="e.g., Developer"
                    />
                </Grid>
                <Grid item xs={12} sm={5}>
                    <TextField
                        label="Skill Name"
                        value={skillName}
                        onChange={(e) => setSkillName(e.target.value)}
                        fullWidth
                        size = 'small'
                        placeholder="e.g., Java"
                    />
                </Grid>
            <Grid item xs={12} sm={2}>
                <Button variant="contained" size = 'medium' onClick={handleApply} sx={{ mr: 1}}>
                    Apply
                </Button>
                <Button variant="outlined" size = 'medium' onClick={handleClear}>
                    Clear
                </Button>
            </Grid>
        </Grid>
    );
};

export default FilterForm;