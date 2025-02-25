package com.example.javalabs.services;

import com.example.javalabs.models.Freelancer;
import java.util.List;

public interface FreelancerService {
    List<Freelancer> getFreelancersByCategory(String category);

    Freelancer getFreelancerById(int id);
}