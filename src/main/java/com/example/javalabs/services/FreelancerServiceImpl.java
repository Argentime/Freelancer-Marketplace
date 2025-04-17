package com.example.javalabs.services;

import com.example.javalabs.cache.FreelancerCache;
import com.example.javalabs.exceptions.NotFoundException;
import com.example.javalabs.exceptions.ValidationException;
import com.example.javalabs.models.Freelancer;
import com.example.javalabs.models.Order;
import com.example.javalabs.models.Skill;
import com.example.javalabs.repositories.FreelancerRepository;
import com.example.javalabs.repositories.OrderRepository;
import com.example.javalabs.repositories.SkillRepository;
import com.example.javalabs.services.impl.FreelancerService;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FreelancerServiceImpl implements FreelancerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreelancerServiceImpl.class);

    private final FreelancerRepository freelancerRepository;
    private final OrderRepository orderRepository;
    private final SkillRepository skillRepository;
    private final FreelancerCache freelancerCache;
    private static final String NF = " not found";

    public FreelancerServiceImpl(FreelancerRepository freelancerRepository,
                                 OrderRepository orderRepository,
                                 SkillRepository skillRepository,
                                 FreelancerCache freelancerCache) {
        this.freelancerRepository = freelancerRepository;
        this.orderRepository = orderRepository;
        this.skillRepository = skillRepository;
        this.freelancerCache = freelancerCache;
    }

    @Override
    public Freelancer createFreelancer(Freelancer freelancer) {
        if (freelancer.getOrders() == null) freelancer.setOrders(new java.util.ArrayList<>());
        if (freelancer.getSkills() == null) freelancer.setSkills(new HashSet<>());
        Freelancer savedFreelancer = freelancerRepository.save(freelancer);
        freelancerCache.clear();
        LOGGER.info("Freelancer created with ID: {}", savedFreelancer.getId());
        return savedFreelancer;
    }

    @Override
    public Freelancer getFreelancerById(Long id) {
        return freelancerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Freelancer with ID " + id + NF));
    }

    @Override
    public Freelancer updateFreelancer(Long id, Freelancer freelancerDetails) {
        Freelancer freelancer = getFreelancerById(id);
        freelancer.setName(freelancerDetails.getName());
        freelancer.setCategory(freelancerDetails.getCategory());
        freelancer.setRating(freelancerDetails.getRating());
        freelancer.setHourlyRate(freelancerDetails.getHourlyRate());
        Freelancer updatedFreelancer = freelancerRepository.save(freelancer);
        freelancerCache.clear();
        LOGGER.info("Freelancer updated with ID: {}", id);
        return updatedFreelancer;
    }

    @Override
    public void deleteFreelancer(Long id) {
        Freelancer freelancer = getFreelancerById(id);
        freelancerRepository.deleteById(freelancer.getId());
        freelancerCache.clear();
        LOGGER.info("Freelancer deleted with ID: {}", id);
    }

    @Override
    public Freelancer addOrderToFreelancer(Long freelancerId, String orderDescription, double orderPrice) {
        Freelancer freelancer = getFreelancerById(freelancerId);
        Order order = new Order(orderDescription, orderPrice);
        order.setFreelancer(freelancer);
        freelancer.getOrders().add(order);
        orderRepository.save(order);
        Freelancer updatedFreelancer = freelancerRepository.save(freelancer);
        freelancerCache.clear();
        LOGGER.info("Order added to freelancer with ID: {}", freelancerId);
        return updatedFreelancer;
    }

    @Override
    public Freelancer addSkillToFreelancer(Long freelancerId, String skillName) {
        Freelancer freelancer = getFreelancerById(freelancerId);
        Optional<Skill> existingSkill = skillRepository.findByName(skillName);
        Skill skill = existingSkill.orElseGet(() -> skillRepository.save(new Skill(skillName)));
        if (!freelancer.getSkills().add(skill)) {
            throw new ValidationException("Skill '" + skillName +
                                          "' is already associated with freelancer with ID " + freelancerId);
        }
        Freelancer updatedFreelancer = freelancerRepository.save(freelancer);
        freelancerCache.clear();
        LOGGER.info("Skill '{}' added to freelancer with ID: {}", skillName, freelancerId);
        return updatedFreelancer;
    }

    @Override
    public void deleteOrderFromFreelancer(Long freelancerId, Long orderId) {
        Freelancer freelancer = getFreelancerById(freelancerId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("Order with ID " + orderId + NF));
        if (order.getFreelancer() == null || !freelancer.getOrders().contains(order)) {
            throw new ValidationException("Order with ID " + orderId +
                                          " does not belong to freelancer with ID " + freelancerId);
        }
        freelancer.getOrders().remove(order);
        orderRepository.delete(order);
        freelancerRepository.save(freelancer);
        freelancerCache.clear();
        LOGGER.info("Deleted order {} from freelancer {}", orderId, freelancerId);
    }

    @Override
    public void deleteSkillFromFreelancer(Long freelancerId, Long skillId) {
        Freelancer freelancer = getFreelancerById(freelancerId);
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new NotFoundException("Skill with ID " + skillId + NF));
        if (!freelancer.getSkills().remove(skill)) {
            throw new ValidationException("Skill with ID " + skillId +
                                          " is not associated with freelancer with ID " + freelancerId);
        }
        freelancerRepository.save(freelancer);
        freelancerCache.clear();
        LOGGER.info("Skill with ID {} deleted from freelancer with ID: {}", skillId, freelancerId);
    }

    @Override
    public List<Freelancer> getFreelancers(String category, String skillName) {
        long startTime;
        List<Freelancer> freelancers;

        if (freelancerCache.containsKey(category, skillName)) {
            startTime = System.nanoTime();
            freelancers = freelancerCache.getFreelancers(category, skillName).stream()
                    .sorted(Comparator.comparingLong(Freelancer::getId))
                    .collect(Collectors.toList());
            long endTime = System.nanoTime();
            LOGGER.info("Data retrieved from cache in {} ns for category: {}, skillName: {}",
                        endTime - startTime, category, skillName);
            return freelancers;
        }

        startTime = System.nanoTime();
        freelancers = freelancerRepository.findByCategoryAndSkill(category, skillName)
                .stream()
                .sorted(Comparator.comparingLong(Freelancer::getId))
                .collect(Collectors.toList());
        long endTime = System.nanoTime();
        LOGGER.info("Data retrieved from database in {} ns for category: {}, skillName: {}",
                    endTime - startTime, category, skillName);

        freelancerCache.putFreelancers(category, skillName, freelancers);
        return freelancers;
    }

    @Override
    @Transactional
    public List<Freelancer> bulkUpsertFreelancers(List<Freelancer> freelancers) {
        if (freelancers == null || freelancers.isEmpty()) {
            LOGGER.warn("Bulk upsert called with empty or null list");
            return List.of();
        }

        List<Freelancer> result = freelancers.stream()
                .filter(f -> f != null && f.getName() != null)
                .map(this::createFreelancer)
                .collect(Collectors.toList());

        LOGGER.info("Processed bulk upsert for {} freelancers", result.size());
        return result;
    }
}