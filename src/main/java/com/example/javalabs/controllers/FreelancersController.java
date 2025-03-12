package com.example.javalabs.controllers;

import com.example.javalabs.models.Freelancer;
import com.example.javalabs.services.FreelancerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class FreelancersController {
    private final FreelancerService freelancerService;

    public FreelancersController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    // Существующие методы остаются без изменений
    @GetMapping("/freelancers")
    public List<Freelancer> getAllFreelancers() {
        return freelancerService.getAllFreelancers();
    }

    @GetMapping("/freelancers/category")
    public List<Freelancer> getFreelancersByCategory(@RequestParam String category) {
        return freelancerService.getFreelancersByCategory(category);
    }

    @GetMapping("/freelancers/{id}")
    public Freelancer getFreelancerById(@PathVariable Long id) {
        try {
            return freelancerService.getFreelancerById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/freelancers")
    @ResponseStatus(HttpStatus.CREATED)
    public Freelancer createFreelancer(@RequestBody Freelancer freelancer) {
        return freelancerService.createFreelancer(freelancer);
    }

    @PutMapping("/freelancers/{id}")
    public Freelancer updateFreelancer(@PathVariable Long id, @RequestBody Freelancer freelancerDetails) {
        return freelancerService.updateFreelancer(id, freelancerDetails);
    }

    @DeleteMapping("/freelancers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFreelancer(@PathVariable Long id) {
        freelancerService.deleteFreelancer(id);
    }

    @PostMapping("/freelancers/{id}/orders")
    public Freelancer addOrderToFreelancer(@PathVariable Long id,
                                           @RequestParam String description,
                                           @RequestParam double price) {
        return freelancerService.addOrderToFreelancer(id, description, price);
    }

    @PostMapping("/freelancers/{id}/skills")
    public Freelancer addSkillToFreelancer(@PathVariable Long id, @RequestParam String skillName) {
        return freelancerService.addSkillToFreelancer(id, skillName);
    }

    @DeleteMapping("/freelancers/{freelancerId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrderFromFreelancer(@PathVariable Long freelancerId, @PathVariable Long orderId) {
        freelancerService.deleteOrderFromFreelancer(freelancerId, orderId);
    }

    @DeleteMapping("/freelancers/{freelancerId}/skills/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSkillFromFreelancer(@PathVariable Long freelancerId, @PathVariable Long skillId) {
        freelancerService.deleteSkillFromFreelancer(freelancerId, skillId);
    }

    // Новый метод
    @GetMapping("/freelancers/by-skill")
    public List<Freelancer> getFreelancersBySkill(@RequestParam String skillName) {
        return freelancerService.getFreelancersBySkill(skillName);
    }
}