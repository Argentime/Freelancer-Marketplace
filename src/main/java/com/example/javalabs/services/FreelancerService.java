package com.example.javalabs.services;

import com.example.javalabs.models.Freelancer;
import java.util.List;

public interface FreelancerService {
    // Основные CRUD операции для фрилансеров
    Freelancer createFreelancer(Freelancer freelancer);
    Freelancer getFreelancerById(Long id);
    List<Freelancer> getFreelancersByCategory(String category);
    Freelancer updateFreelancer(Long id, Freelancer freelancerDetails);
    void deleteFreelancer(Long id);

    // Дополнительные методы для работы со связями
    Freelancer addOrderToFreelancer(Long freelancerId, String orderDescription, double orderPrice);
    Freelancer addSkillToFreelancer(Long freelancerId, String skillName);
    List<Freelancer> getAllFreelancers();
}