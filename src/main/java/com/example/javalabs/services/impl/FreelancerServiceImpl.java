package com.example.javalabs.services.impl;

import com.example.javalabs.models.Freelancer;
import com.example.javalabs.models.Order;
import com.example.javalabs.models.Skill;
import com.example.javalabs.repositories.FreelancerRepository;
import com.example.javalabs.repositories.OrderRepository;
import com.example.javalabs.repositories.SkillRepository;
import com.example.javalabs.services.FreelancerService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FreelancerServiceImpl implements FreelancerService {
    private final FreelancerRepository freelancerRepository;
    private final OrderRepository orderRepository;
    private final SkillRepository skillRepository;

    public FreelancerServiceImpl(FreelancerRepository freelancerRepository,
                                 OrderRepository orderRepository,
                                 SkillRepository skillRepository) {
        this.freelancerRepository = freelancerRepository;
        this.orderRepository = orderRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public Freelancer createFreelancer(Freelancer freelancer) {
        if (freelancer.getOrders() == null) {
            freelancer.setOrders(new java.util.ArrayList<>());
        }
        if (freelancer.getSkills() == null) {
            freelancer.setSkills(new HashSet<>());
        }
        return freelancerRepository.save(freelancer);
    }

    @Override
    public Freelancer getFreelancerById(Long id) {
        return freelancerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Freelancer with ID " + id + " not found"));
    }

    @Override
    public List<Freelancer> getFreelancersByCategory(String category) {
        return freelancerRepository.findByCategory(category);
    }

    @Override
    public Freelancer updateFreelancer(Long id, Freelancer freelancerDetails) {
        Freelancer freelancer = getFreelancerById(id);
        freelancer.setName(freelancerDetails.getName());
        freelancer.setCategory(freelancerDetails.getCategory());
        freelancer.setRating(freelancerDetails.getRating());
        freelancer.setHourlyRate(freelancerDetails.getHourlyRate());
        return freelancerRepository.save(freelancer);
    }

    @Override
    public void deleteFreelancer(Long id) {
        Freelancer freelancer = getFreelancerById(id);
        freelancerRepository.delete(freelancer);
    }

    @Override
    public Freelancer addOrderToFreelancer(Long freelancerId, String orderDescription, double orderPrice) {
        Freelancer freelancer = getFreelancerById(freelancerId);
        Order order = new Order(orderDescription, orderPrice);
        order.setFreelancer(freelancer);
        freelancer.getOrders().add(order);
        orderRepository.save(order);
        return freelancerRepository.save(freelancer);
    }

    @Override
    public Freelancer addSkillToFreelancer(Long freelancerId, String skillName) {
        Freelancer freelancer = getFreelancerById(freelancerId);

        Optional<Skill> existingSkill = skillRepository.findByName(skillName);
        Skill skill = existingSkill.orElseGet(() -> {
            Skill newSkill = new Skill(skillName);
            return skillRepository.save(newSkill);
        });

        freelancer.getSkills().add(skill);
        return freelancerRepository.save(freelancer);
    }

    @Override
    public List<Freelancer> getAllFreelancers() {
        return freelancerRepository.findAll();
    }
}