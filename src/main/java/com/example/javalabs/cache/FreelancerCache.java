package com.example.javalabs.cache;

import com.example.javalabs.models.Freelancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FreelancerCache {
    private final Map<String, List<Freelancer>> cache;

    public FreelancerCache() {
        this.cache = new HashMap<>();
    }

    private String generateKey(String category, String skillName) {
        return (category != null ? category : "null") + "_" + (skillName != null ? skillName : "null");
    }

    public List<Freelancer> getFreelancers(String category, String skillName) {
        return cache.get(generateKey(category, skillName));
    }

    public void putFreelancers(String category, String skillName, List<Freelancer> freelancers) {
        cache.put(generateKey(category, skillName), freelancers);
    }

    public void clear() {
        cache.clear();
    }

    public boolean containsKey(String category, String skillName) {
        return cache.containsKey(generateKey(category, skillName));
    }
}