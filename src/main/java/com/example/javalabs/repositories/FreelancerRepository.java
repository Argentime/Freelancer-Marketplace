package com.example.javalabs.repositories;

import com.example.javalabs.models.Freelancer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    List<Freelancer> findByCategory(String category);
}