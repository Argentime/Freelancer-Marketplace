package com.example.javalabs.controllers;

import com.example.javalabs.models.Freelancer;
import com.example.javalabs.services.FreelancerService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class FreelancersController {
    private final FreelancerService freelancerService;

    public FreelancersController(FreelancerService freelancerService) {
        this.freelancerService = freelancerService;
    }

    @GetMapping("/freelancers")
    public List<Freelancer> getFreelancers(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String skillName) {
        return freelancerService.getFreelancers(category, skillName);
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
}