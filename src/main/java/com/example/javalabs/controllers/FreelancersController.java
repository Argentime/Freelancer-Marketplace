package com.example.javalabs.controllers;

import com.example.javalabs.models.Freelancer;
import com.example.javalabs.services.impl.FreelancerService;
import com.example.javalabs.services.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "Freelancers", description = "API for managing freelancers")
@CrossOrigin(origins = {"http://localhost:3000", "https://freelance-marketplace-frontend.up.railway.app/"})
public class FreelancersController {
    private final FreelancerService freelancerService;

    public FreelancersController(FreelancerService freelancerService, LogService logService) {
        this.freelancerService = freelancerService;
    }

    @GetMapping("/freelancers")
    @Operation(summary = "Get freelancers", description = "Retrieve freelancers by category and/or skill")
    @ApiResponse(responseCode = "200", description = "List of freelancers")
    public ResponseEntity<List<Freelancer>> getFreelancers(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String skillName) {
        List<Freelancer> freelancers = freelancerService.getFreelancers(category, skillName);
        return ResponseEntity.ok(freelancers);
    }

    @PostMapping("/freelancers/bulk")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Bulk upsert freelancers", description = "Create or update multiple freelancers")
    @ApiResponse(responseCode = "200", description = "Freelancers processed")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<List<Freelancer>> bulkUpsertFreelancers(@Valid @RequestBody List<Freelancer> freelancers) {
        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(freelancers);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/freelancers/{id}")
    @Operation(summary = "Get freelancer by ID", description = "Retrieve a freelancer by their ID")
    @ApiResponse(responseCode = "200", description = "Freelancer found")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public ResponseEntity<Freelancer> getFreelancerById(@PathVariable Long id) {
        Freelancer freelancer = freelancerService.getFreelancerById(id);
        if (freelancer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(freelancer);
    }

    @PostMapping("/freelancers")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create freelancer", description = "Create a new freelancer")
    @ApiResponse(responseCode = "201", description = "Freelancer created")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public ResponseEntity<Freelancer> createFreelancer(@Valid @RequestBody Freelancer freelancer) {
        Freelancer created = freelancerService.createFreelancer(freelancer);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/freelancers/{id}")
    @Operation(summary = "Update freelancer", description = "Update an existing freelancer")
    @ApiResponse(responseCode = "200", description = "Freelancer updated")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public ResponseEntity<Freelancer> updateFreelancer(@PathVariable Long id,
                                                       @Valid @RequestBody Freelancer freelancerDetails) {
        Freelancer updated = freelancerService.updateFreelancer(id, freelancerDetails);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/freelancers/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete freelancer", description = "Delete a freelancer by ID")
    @ApiResponse(responseCode = "204", description = "Freelancer deleted")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public ResponseEntity<Void> deleteFreelancer(@PathVariable Long id) {
        freelancerService.deleteFreelancer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/freelancers/{id}/orders")
    @Operation(summary = "Add order to freelancer", description = "Add an order to a freelancer")
    @ApiResponse(responseCode = "200", description = "Order added")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public ResponseEntity<Freelancer> addOrderToFreelancer(
            @PathVariable Long id,
            @RequestParam @NotBlank(message = "Description cannot be blank") String description,
            @RequestParam @Positive(message = "Price must be positive") double price) {
        Freelancer updated = freelancerService.addOrderToFreelancer(id, description, price);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/freelancers/{id}/skills")
    @Operation(summary = "Add skill to freelancer", description = "Add a skill to a freelancer")
    @ApiResponse(responseCode = "200", description = "Skill added")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "404", description = "Freelancer not found")
    public ResponseEntity<Freelancer> addSkillToFreelancer(
            @PathVariable Long id,
            @RequestParam @NotBlank(message = "Skill name cannot be blank") String skillName) {
        Freelancer updated = freelancerService.addSkillToFreelancer(id, skillName);
        if (updated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/freelancers/{freelancerId}/orders/{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete order from freelancer", description = "Remove an order from a freelancer")
    @ApiResponse(responseCode = "204", description = "Order deleted")
    @ApiResponse(responseCode = "404", description = "Freelancer or order not found")
    @ApiResponse(responseCode = "400", description = "Order does not belong to freelancer")
    public ResponseEntity<Void> deleteOrderFromFreelancer(@PathVariable Long freelancerId, @PathVariable Long orderId) {
        freelancerService.deleteOrderFromFreelancer(freelancerId, orderId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/freelancers/{freelancerId}/skills/{skillId}")
    @Operation(summary = "Delete skill from freelancer", description = "Remove a skill from a freelancer")
    @ApiResponse(responseCode = "204", description = "Skill deleted")
    @ApiResponse(responseCode = "404", description = "Freelancer or skill not found")
    @ApiResponse(responseCode = "400", description = "Skill not associated with freelancer")
    public ResponseEntity<Void> deleteSkillFromFreelancer(@PathVariable Long freelancerId, @PathVariable Long skillId) {
        freelancerService.deleteSkillFromFreelancer(freelancerId, skillId);
        return ResponseEntity.noContent().build();
    }

}