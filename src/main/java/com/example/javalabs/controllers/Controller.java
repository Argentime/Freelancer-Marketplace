package com.example.javalabs.controllers;

import com.example.javalabs.models.Freelancer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api")
public class Controller {
    @GetMapping("/freelancers")
    public List<Freelancer> getFreelancers(@RequestParam String category) {
        switch (category.toLowerCase()) {
            case "design":
                return List.of(
                        new Freelancer("Alice", "Design", 4.8, 120),
                        new Freelancer("Bob", "Design", 4.5, 95)
                );
            case "development":
                return List.of(
                        new Freelancer("Charlie", "Development", 4.7, 110),
                        new Freelancer("Dave", "Development", 4.6, 105)
                );
            default:
                return List.of();
        }
    }
}
