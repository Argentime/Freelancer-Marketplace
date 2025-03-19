package com.example.javalabs.cache;

import com.example.javalabs.models.Freelancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class FreelancerCache {
    private static final Logger logger = LoggerFactory.getLogger(FreelancerCache.class);
    private static final int MAX_CACHE_SIZE = 100;

    private final Map<String, List<Freelancer>> cache;

    public FreelancerCache() {
        this.cache = new LinkedHashMap<String, List<Freelancer>>(MAX_CACHE_SIZE, 0.75f, false) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, List<Freelancer>> eldest) {
                if (size() > MAX_CACHE_SIZE) {
                    logger.info("Cache size limit ({}) reached, removing oldest entry: {}", MAX_CACHE_SIZE, eldest.getKey());
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
            key.replaceAll("[\n\r]", "_");
            logger.info("Cache hit for key: {}", key);
        }
        return result;
    }

    public void putFreelancers(String category, String skillName, List<Freelancer> freelancers) {
        String key = generateKey(category, skillName);
        cache.put(key, freelancers);
        logger.info("Added to cache: key={}, size={}", key, cache.size());
    }

    public void clear() {
        logger.info("Clearing cache, previous size: {}", cache.size());
        cache.clear();
    }

    public boolean containsKey(String category, String skillName) {
        return cache.containsKey(generateKey(category, skillName));
    }
}