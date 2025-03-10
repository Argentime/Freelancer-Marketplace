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

    // Получить всех фрилансеров
    @GetMapping("/freelancers")
    public List<Freelancer> getFreelancers(@RequestParam(required = false) String category) {
        if (category != null) {
            return freelancerService.getFreelancersByCategory(category);
        }
        return freelancerService.getAllFreelancers();
    }

    // Получить фрилансера по ID
    @GetMapping("/freelancers/{id}")
    public Freelancer getFreelancerById(@PathVariable Long id) {
        try {
            return freelancerService.getFreelancerById(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Создать нового фрилансера
    @PostMapping("/freelancers")
    @ResponseStatus(HttpStatus.CREATED)
    public Freelancer createFreelancer(@RequestBody Freelancer freelancer) {
        return freelancerService.createFreelancer(freelancer);
    }

    // Обновить фрилансера
    @PutMapping("/freelancers/{id}")
    public Freelancer updateFreelancer(@PathVariable Long id, @RequestBody Freelancer freelancerDetails) {
        return freelancerService.updateFreelancer(id, freelancerDetails);
    }

    // Удалить фрилансера
    @DeleteMapping("/freelancers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFreelancer(@PathVariable Long id) {
        freelancerService.deleteFreelancer(id);
    }

    // Добавить заказ фрилансеру
    @PostMapping("/freelancers/{id}/orders")
    public Freelancer addOrderToFreelancer(@PathVariable Long id,
                                           @RequestParam String description,
                                           @RequestParam double price) {
        return freelancerService.addOrderToFreelancer(id, description, price);
    }

    // Добавить навык фрилансеру
    @PostMapping("/freelancers/{id}/skills")
    public Freelancer addSkillToFreelancer(@PathVariable Long id, @RequestParam String skillName) {
        return freelancerService.addSkillToFreelancer(id, skillName);
    }
}