package com.example.javalabs.cache;

import com.example.javalabs.models.Freelancer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class FreelancerCacheTest {

    private FreelancerCache freelancerCache;
    private Freelancer freelancer;

    @BeforeEach
    void setUp() {
        freelancerCache = new FreelancerCache();
        freelancer = new Freelancer();
        freelancer.setId(1L);
        freelancer.setName("Alice");
    }

    @Test
    void getFreelancers_cacheHit_returnsFreelancers() {
        List<Freelancer> freelancers = Collections.singletonList(freelancer);
        freelancerCache.putFreelancers("design", "Java", freelancers);

        List<Freelancer> result = freelancerCache.getFreelancers("design", "Java");

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals("Alice", result.get(0).getName());
    }

    @Test
    void getFreelancers_cacheMiss_returnsNull() {
        List<Freelancer> result = freelancerCache.getFreelancers("design", "Java");

        Assertions.assertNull(result);
    }

    @Test
    void putFreelancers_withinLimit_addsToCache() {
        List<Freelancer> freelancers = Collections.singletonList(freelancer);

        freelancerCache.putFreelancers("design", "Java", freelancers);

        Assertions.assertTrue(freelancerCache.containsKey("design", "Java"));
        Assertions.assertEquals(freelancers, freelancerCache.getFreelancers("design", "Java"));
    }

    @Test
    void putFreelancers_exceedsListLimit_truncatesList() {
        List<Freelancer> largeList = new ArrayList<>();
        for (int i = 0; i < 1500; i++) {
            Freelancer f = new Freelancer();
            f.setId((long) i);
            largeList.add(f);
        }

        freelancerCache.putFreelancers("design", "Java", largeList);

        List<Freelancer> result = freelancerCache.getFreelancers("design", "Java");
        Assertions.assertNotNull(result);
        Assertions.assertEquals(1000, result.size());
    }

    @Test
    void putFreelancers_exceedsCacheSize_removesOldest() {
        // Заполняем кэш до предела (MAX_CACHE_SIZE = 100)
        for (int i = 0; i < 101; i++) {
            freelancerCache.putFreelancers("category" + i, "skill" + i, Collections.singletonList(freelancer));
        }

        // Проверяем, что самая старая запись удалена
        Assertions.assertFalse(freelancerCache.containsKey("category0", "skill0"));
        Assertions.assertTrue(freelancerCache.containsKey("category100", "skill100"));
    }

    @Test
    void clear_removesAllEntries() {
        freelancerCache.putFreelancers("design", "Java", Collections.singletonList(freelancer));
        freelancerCache.putFreelancers("dev", "Python", Collections.singletonList(freelancer));

        freelancerCache.clear();

        Assertions.assertFalse(freelancerCache.containsKey("design", "Java"));
        Assertions.assertFalse(freelancerCache.containsKey("dev", "Python"));
    }

    @Test
    void containsKey_existingKey_returnsTrue() {
        freelancerCache.putFreelancers("design", "Java", Collections.singletonList(freelancer));

        Assertions.assertTrue(freelancerCache.containsKey("design", "Java"));
    }

    @Test
    void containsKey_nonExistingKey_returnsFalse() {
        Assertions.assertFalse(freelancerCache.containsKey("design", "Java"));
    }

    @Test
    void generateKey_nullValues_generatesCorrectKey() {
        freelancerCache.putFreelancers(null, null, Collections.singletonList(freelancer));

        Assertions.assertTrue(freelancerCache.containsKey(null, null));
        Assertions.assertNotNull(freelancerCache.getFreelancers(null, null));
    }
}