package com.example.javalabs.cache;

import com.example.javalabs.models.Freelancer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FreelancerCache {
    private static final Logger CACHE_LOGGER = LoggerFactory.getLogger(FreelancerCache.class);
    private static final int MAX_CACHE_SIZE = 100;
    private static final int MAX_FREELANCERS_PER_LIST = 1000;

    private final Map<String, List<Freelancer>> cache;

    public FreelancerCache() {
        this.cache = new LinkedHashMap<String, List<Freelancer>>(MAX_CACHE_SIZE, 0.75f, false) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, List<Freelancer>> eldest) {
                if (size() > MAX_CACHE_SIZE) {
                    CACHE_LOGGER.info("Cache size limit ({}) reached, removing oldest entry: {}",
                                      MAX_CACHE_SIZE, eldest.getKey());
                    return true;
                }
                return false;
            }
        };
    }

    private String generateKey(String category, String skillName) {
        return (category != null ? category : "null") + "_" + (skillName != null ? skillName : "null");
    }

    public List<Freelancer> getFreelancers(String category, String skillName) {
        String key = generateKey(category, skillName);
        List<Freelancer> result = cache.get(key);
        if (result != null) {
            CACHE_LOGGER.info("Cache hit for hash: {}", cache.get(key).hashCode());
        }
        return result;
    }

    public void putFreelancers(String category, String skillName, List<Freelancer> freelancers) {
        if (freelancers.size() > MAX_FREELANCERS_PER_LIST) {
            CACHE_LOGGER.warn("List size exceeds limit ({}), truncating to {} elements",
                              MAX_FREELANCERS_PER_LIST, MAX_FREELANCERS_PER_LIST);
            freelancers = freelancers.subList(0, MAX_FREELANCERS_PER_LIST);
        }
        String key = generateKey(category, skillName);
        cache.put(key, freelancers);
        CACHE_LOGGER.info("Added to cache: size={}", cache.size());
    }

    public void clear() {
        CACHE_LOGGER.info("Clearing cache, previous size: {}", cache.size());
        cache.clear();
    }

    public boolean containsKey(String category, String skillName) {
        return cache.containsKey(generateKey(category, skillName));
    }
}