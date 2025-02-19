package com.example.javalabs.controllers;

import com.example.javalabs.models.Freelancer;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {
    //jg
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
                for(int i=0; i<freelancersList.size(); i++) {
                    if (freelancersList.get(i).getCategory().equals(category)) {
                        queue.add(freelancersList.get(i));
                    }
                }
                if (queue.isEmpty()) {
                    return null;
                }
        return queue.stream().toList();
    }

    @GetMapping("/freelancers/{id}") // http://localhost:8080/api/freelancers/numb
    public Freelancer getFreelancerById(@PathVariable int id) {
        return freelancersList.get(id);
    }
}
