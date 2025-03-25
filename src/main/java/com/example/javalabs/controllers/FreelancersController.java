package com.example.javalabs.controllers;

import com.example.javalabs.exceptions.ValidationException;
import com.example.javalabs.models.Freelancer;
import com.example.javalabs.services.FreelancerService;

import java.io.IOException;
import java.util.List;

import com.example.javalabs.services.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api")
@Tag(name = "Freelancers", description = "API for managing freelancers")
public class FreelancersController {
    private final FreelancerService freelancerService;
    private final LogService logService;

    public FreelancersController(FreelancerService freelancerService, LogService logService) {
        this.freelancerService = freelancerService;
        this.logService = logService;
    }

    @GetMapping("/freelancers")
    @Operation(summary = "Get freelancers", description = "Retrieve freelancers by category and/or skill")
    @ApiResponse(responseCode = "200", description = "List of freelancers")
    public List<Freelancer> getFreelancers(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String skillName) {
        return freelancerService.getFreelancers(category, skillName);
    }

    @GetMapping("/freelancers/{id}")
    @Operation(summary = "Get freelancer by ID", description = "Retrieve a freelancer by their ID")
    @ApiResponse(responseCode = "200", description = "Freelancer found")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public Freelancer getFreelancerById(@PathVariable Long id) {
        return freelancerService.getFreelancerById(id);
    }

    @PostMapping("/freelancers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create freelancer", description = "Create a new freelancer")
    @ApiResponse(responseCode = "201", description = "Freelancer created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public Freelancer createFreelancer(@Valid @RequestBody Freelancer freelancer) {
        return freelancerService.createFreelancer(freelancer);
    }

    @PutMapping("/freelancers/{id}")
    @Operation(summary = "Update freelancer", description = "Update an existing freelancer")
    @ApiResponse(responseCode = "200", description = "Freelancer updated")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public Freelancer updateFreelancer(@PathVariable Long id, @Valid @RequestBody Freelancer freelancerDetails) {
        return freelancerService.updateFreelancer(id, freelancerDetails);
    }

    @DeleteMapping("/freelancers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete freelancer", description = "Delete a freelancer by ID")
    @ApiResponse(responseCode = "204", description = "Freelancer deleted")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public void deleteFreelancer(@PathVariable Long id) {
        freelancerService.deleteFreelancer(id);
    }

    @PostMapping("/freelancers/{id}/orders")
    @Operation(summary = "Add order to freelancer", description = "Add an order to a freelancer")
    @ApiResponse(responseCode = "200", description = "Order added")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public Freelancer addOrderToFreelancer(
            @PathVariable Long id,
            @RequestParam @NotBlank(message = "Description cannot be blank") String description,
            @RequestParam @Positive(message = "Price must be positive") double price) {
        return freelancerService.addOrderToFreelancer(id, description, price);
    }

    @PostMapping("/freelancers/{id}/skills")
    @Operation(summary = "Add skill to freelancer", description = "Add a skill to a freelancer")
    @ApiResponse(responseCode = "200", description = "Skill added")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public Freelancer addSkillToFreelancer(
            @PathVariable Long id,
            @RequestParam @NotBlank(message = "Skill name cannot be blank") String skillName) {
        return freelancerService.addSkillToFreelancer(id, skillName);
    }

    @DeleteMapping("/freelancers/{freelancerId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete order from freelancer", description = "Remove an order from a freelancer")
    @ApiResponse(responseCode = "204", description = "Order deleted")
    @ApiResponse(responseCode = "404", description = "Freelancer or order not found")
    @ApiResponse(responseCode = "400", description = "Order does not belong to freelancer")
    public void deleteOrderFromFreelancer(@PathVariable Long freelancerId, @PathVariable Long orderId) {
        freelancerService.deleteOrderFromFreelancer(freelancerId, orderId);
    }

    @DeleteMapping("/freelancers/{freelancerId}/skills/{skillId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete skill from freelancer", description = "Remove a skill from a freelancer")
    @ApiResponse(responseCode = "204", description = "Skill deleted")
    @ApiResponse(responseCode = "404", description = "Freelancer or skill not found")
    @ApiResponse(responseCode = "400", description = "Skill not associated with freelancer")
    public void deleteSkillFromFreelancer(@PathVariable Long freelancerId, @PathVariable Long skillId) {
        freelancerService.deleteSkillFromFreelancer(freelancerId, skillId);
    }

    @GetMapping("/logs")
    @Operation(summary = "Get logs", description = "Retrieve logs, optionally filtered by date (yyyy-MM-dd) and/or level (INFO, WARN, ERROR)")
    @ApiResponse(responseCode = "200", description = "Logs retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid date or file error")
    public ResponseEntity<String> getLogs(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String level) throws IOException {
        try {
            String logs = logService.getLogs(date, level);
            return ResponseEntity.ok(logs);
        } catch (IOException e) {
            throw new ValidationException("Failed to read logs" + (date != null ? " for date: " + date : "") +
                    (level != null ? " with level: " + level : ""));
        }
    }
}