package com.example.javalabs.controllers;

import com.example.javalabs.models.Freelancer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class Controller {
    private List<Freelancer> freelancersList;

    private void createTestFreelancers() {
        this.freelancersList = new ArrayList<>();
        freelancersList.add(new Freelancer("Alice", "design", 4.8, 120));
        freelancersList.add(new Freelancer("Bob", "design", 4.5, 95));
        freelancersList.add(new Freelancer("Charlie", "development", 4.7, 110));
        freelancersList.add(new Freelancer("Dave", "development", 4.6, 105));
    }

    public Controller() {
        createTestFreelancers();
    }

    @GetMapping("/freelancers") // http://localhost:8080/api/freelancers?category=cat
    public List<Freelancer> getFreelancers(@RequestParam String category) {
        Queue<Freelancer> queue = new LinkedList<>();
        for (int i = 0; i < freelancersList.size(); i++) {
            if (freelancersList.get(i).getCategory().equals(category)) {
                queue.add(freelancersList.get(i));
            }
        }
        if (queue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Freenacers matching the parameters");
        }
        return queue.stream().toList();
    }

    @GetMapping("/freelancers/{id}") // http://localhost:8080/api/freelancers/numb
    public Freelancer getFreelancerById(@PathVariable int id) {
        if ((id >= 0) && (id <= freelancersList.size())) {
            return freelancersList.get(id);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Freelancer not found");
    }
}
