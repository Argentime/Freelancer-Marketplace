package com.example.javalabs.cache;

import com.example.javalabs.models.Freelancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FreelancerCache {
    private final Map<String, List<Freelancer>> cacheBySkill;

    public FreelancerCache() {
        this.cacheBySkill = new HashMap<>();
    }

    public List<Freelancer> getFreelancersBySkill(String skillName) {
        return cacheBySkill.get(skillName);
    }

    public void putFreelancersBySkill(String skillName, List<Freelancer> freelancers) {
        cacheBySkill.put(skillName, freelancers);
    }

    public void clear() {
        cacheBySkill.clear();
    }

    public boolean containsKey(String skillName) {
        return cacheBySkill.containsKey(skillName);
    }
}