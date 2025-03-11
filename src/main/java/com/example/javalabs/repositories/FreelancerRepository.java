package com.example.javalabs.repositories;

import com.example.javalabs.models.Freelancer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerRepository extends JpaRepository<Freelancer, Long> {
    List<Freelancer> findByCategory(String category);
}