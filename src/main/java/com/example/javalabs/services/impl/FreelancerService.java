package com.example.javalabs.services.impl;

import com.example.javalabs.models.Freelancer;
import java.util.List;

public interface FreelancerService {
    Freelancer createFreelancer(Freelancer freelancer);

    Freelancer getFreelancerById(Long id);

    Freelancer updateFreelancer(Long id, Freelancer freelancerDetails);

    void deleteFreelancer(Long id);

    Freelancer addOrderToFreelancer(Long freelancerId, String orderDescription, double orderPrice);

    Freelancer addSkillToFreelancer(Long freelancerId, String skillName);

    void deleteOrderFromFreelancer(Long freelancerId, Long orderId);

    void deleteSkillFromFreelancer(Long freelancerId, Long skillId);

    List<Freelancer> getFreelancers(String category, String skillName);

    List<Freelancer> bulkUpsertFreelancers(List<Freelancer> freelancers);
}