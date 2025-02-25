package com.example.javalabs.controllers;

import com.example.javalabs.models.Freelancer;
import com.example.javalabs.services.FreelancerService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class FreelancerController {
    private final FreelancerService freelancerService;

    public FreelancerController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @GetMapping("/freelancers") // http://localhost:8080/api/freelancers?category=cat
    public List<Freelancer> getFreelancers(@RequestParam String category) {
        try {
            return freelancerService.getFreelancersByCategory(category);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/freelancers/{id}") // http://localhost:8080/api/freelancers/numb
    public Freelancer getFreelancerById(@PathVariable int id) {
        try {
            return freelancerService.getFreelancerById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}