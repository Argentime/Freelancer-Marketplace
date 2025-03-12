package com.example.javalabs.services.impl;

import com.example.javalabs.cache.FreelancerCache;
import com.example.javalabs.models.Freelancer;
import com.example.javalabs.models.Order;
import com.example.javalabs.models.Skill;
import com.example.javalabs.repositories.FreelancerRepository;
import com.example.javalabs.repositories.OrderRepository;
import com.example.javalabs.repositories.SkillRepository;
import com.example.javalabs.services.FreelancerService;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FreelancerServiceImpl implements FreelancerService {
    private final FreelancerRepository freelancerRepository;
    private final OrderRepository orderRepository;
    private final SkillRepository skillRepository;
    private final FreelancerCache freelancerCache;

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
        return savedFreelancer;
    }

    @Override
    public Freelancer getFreelancerById(Long id) {
        return freelancerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer with ID " + id + " not found"));
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
        return updatedFreelancer;
    }

    @Override
    public void deleteFreelancer(Long id) {
        Freelancer freelancer = getFreelancerById(id);
        freelancerRepository.delete(freelancer);
        freelancerCache.clear();
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
        return updatedFreelancer;
    }

    @Override
    public Freelancer addSkillToFreelancer(Long freelancerId, String skillName) {
        Freelancer freelancer = getFreelancerById(freelancerId);
        Optional<Skill> existingSkill = skillRepository.findByName(skillName);
        Skill skill = existingSkill.orElseGet(() -> skillRepository.save(new Skill(skillName)));
        freelancer.getSkills().add(skill);
        Freelancer updatedFreelancer = freelancerRepository.save(freelancer);
        freelancerCache.clear();
        return updatedFreelancer;
    }

    @Override
    public void deleteOrderFromFreelancer(Long freelancerId, Long orderId) {
        Freelancer freelancer = getFreelancerById(freelancerId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with ID " + orderId + " not found"));
        if (!order.getFreelancer().getId().equals(freelancerId)) {
            throw new IllegalArgumentException("Order with ID " + orderId +
                                               " does not belong to Freelancer with ID " + freelancerId);
        }
        freelancer.getOrders().remove(order);
        orderRepository.delete(order);
        freelancerRepository.save(freelancer);
        freelancerCache.clear();
    }

    @Override
    public void deleteSkillFromFreelancer(Long freelancerId, Long skillId) {
        Freelancer freelancer = getFreelancerById(freelancerId);
        Skill skill = skillRepository.findById(skillId)
                .orElseThrow(() -> new IllegalArgumentException("Skill with ID " + skillId + " not found"));
        if (!freelancer.getSkills().remove(skill)) {
            throw new IllegalArgumentException("Skill with ID " + skillId +
                                               " is not associated with Freelancer with ID " + freelancerId);
        }
        freelancerRepository.save(freelancer);
        freelancerCache.clear();
    }

    @Override
    public List<Freelancer> getFreelancers(String category, String skillName) {
        if (freelancerCache.containsKey(category, skillName)) {
            return freelancerCache.getFreelancers(category, skillName).stream()
                    .sorted(Comparator.comparingLong(Freelancer::getId))
                    .collect(Collectors.toList());
        }
        List<Freelancer> freelancers = freelancerRepository.findByCategoryAndSkill(category, skillName)
                .stream()
                .sorted(Comparator.comparingLong(Freelancer::getId)) // Сортировка по id
                .collect(Collectors.toList());
        freelancerCache.putFreelancers(category, skillName, freelancers);
        return freelancers;
    }
}