package com.example.javalabs.services;

import com.example.javalabs.models.Freelancer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.springframework.stereotype.Service;

@Service
public class FreelancerServiceImpl implements FreelancerService {
    private List<Freelancer> freelancersList;

    public FreelancerServiceImpl() {
        initializeTestData();
    }

    private void initializeTestData() {
        this.freelancersList = new ArrayList<>();
        freelancersList.add(new Freelancer("Alice", "design", 4.8, 120));
        freelancersList.add(new Freelancer("Bob", "design", 4.5, 95));
        freelancersList.add(new Freelancer("Charlie", "development", 4.7, 110));
        freelancersList.add(new Freelancer("Dave", "development", 4.6, 105));
    }

    @Override
    public List<Freelancer> getFreelancersByCategory(String category) {
        Queue<Freelancer> queue = new LinkedList<>();
        for (Freelancer freelancer : freelancersList) {
            if (freelancer.getCategory().equals(category)) {
                queue.add(freelancer);
            }
        }
        if (queue.isEmpty()) {
            throw new IllegalArgumentException("No Freelancers matching the parameters");
        }
        return queue.stream().toList();
    }

    @Override
    public Freelancer getFreelancerById(int id) {
        if (id >= 0 && id < freelancersList.size()) {
            return freelancersList.get(id);
        }
        throw new IllegalArgumentException("Freelancer not found");
    }
}