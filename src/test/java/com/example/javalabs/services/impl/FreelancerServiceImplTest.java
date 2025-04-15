package com.example.javalabs.services.impl;

import com.example.javalabs.cache.FreelancerCache;
import com.example.javalabs.exceptions.NotFoundException;
import com.example.javalabs.exceptions.ValidationException;
import com.example.javalabs.models.Freelancer;
import com.example.javalabs.models.Order;
import com.example.javalabs.models.Skill;
import com.example.javalabs.repositories.FreelancerRepository;
import com.example.javalabs.repositories.OrderRepository;
import com.example.javalabs.repositories.SkillRepository;
import com.example.javalabs.services.FreelancerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerServiceImplTest {

    @Mock
    private FreelancerRepository freelancerRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private FreelancerCache freelancerCache;

    @InjectMocks
    private FreelancerServiceImpl freelancerService;

    private Freelancer freelancer;
    private Skill skill;
    private Order order;

    @BeforeEach
    void setUp() {
        freelancer = new Freelancer();
        freelancer.setId(1L);
        freelancer.setName("Alice");
        freelancer.setCategory("design");
        freelancer.setRating(4.5);
        freelancer.setHourlyRate(25.0);
        freelancer.setOrders(new ArrayList<>());
        freelancer.setSkills(new HashSet<>());

        skill = new Skill();
        skill.setId(1L);
        skill.setName("Java");

        order = new Order();
        order.setId(1L);
        order.setDescription("Website design");
        order.setPrice(100.0);
        order.setFreelancer(freelancer);
    }

    @Test
    void getFreelancers_noFilters_cacheMiss_returnsAll() {
        List<Freelancer> freelancers = List.of(freelancer);
        when(freelancerCache.getFreelancers(null, null)).thenReturn(null);
        when(freelancerRepository.findAll()).thenReturn(freelancers);

        List<Freelancer> result = freelancerService.getFreelancers(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(freelancerCache).putFreelancers(null, null, freelancers);
    }

    @Test
    void getFreelancers_noFilters_cacheHit_returnsCached() {
        List<Freelancer> freelancers = List.of(freelancer);
        when(freelancerCache.getFreelancers(null, null)).thenReturn(freelancers);

        List<Freelancer> result = freelancerService.getFreelancers(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(freelancerRepository, never()).findAll();
    }

    @Test
    void getFreelancers_withCategory_cacheMiss_returnsFiltered() {
        List<Freelancer> freelancers = List.of(freelancer);
        when(freelancerCache.getFreelancers("design", null)).thenReturn(null);
        when(freelancerRepository.findByCategoryAndSkill("design",null)).thenReturn(freelancers);

        List<Freelancer> result = freelancerService.getFreelancers("design", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("design", result.get(0).getCategory());
        verify(freelancerCache).putFreelancers("design", null, freelancers);
    }

    @Test
    void getFreelancerById_found_returnsFreelancer() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));

        Freelancer result = freelancerService.getFreelancerById(1L);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
    }

    @Test
    void getFreelancerById_notFound_throwsException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> freelancerService.getFreelancerById(1L));
    }

    @Test
    void createFreelancer_valid_savesFreelancer() {
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancer);

        Freelancer result = freelancerService.createFreelancer(freelancer);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(freelancerCache).clear();
    }

    @Test
    void updateFreelancer_valid_updatesFreelancer() {
        Freelancer updatedDetails = new Freelancer();
        updatedDetails.setName("Bob");
        updatedDetails.setCategory("dev");
        updatedDetails.setRating(4.8);
        updatedDetails.setHourlyRate(30.0);

        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancer);

        Freelancer result = freelancerService.updateFreelancer(1L, updatedDetails);

        assertNotNull(result);
        assertEquals("Bob", result.getName());
        assertEquals("dev", result.getCategory());
        verify(freelancerCache).clear();
    }

    @Test
    void updateFreelancer_notFound_throwsException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> freelancerService.updateFreelancer(1L, freelancer));
    }

    @Test
    void deleteFreelancer_exists_deletesFreelancer() {
        when(freelancerRepository.existsById(1L)).thenReturn(true);

        freelancerService.deleteFreelancer(1L);

        verify(freelancerRepository).deleteById(1L);
        verify(freelancerCache).clear();
    }

    @Test
    void deleteFreelancer_notFound_throwsException() {
        when(freelancerRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> freelancerService.deleteFreelancer(1L));
    }

    @Test
    void addOrderToFreelancer_valid_addsOrder() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancer);

        Freelancer result = freelancerService.addOrderToFreelancer(1L, "Website design", 100.0);

        assertNotNull(result.getOrders());
        assertEquals(1, result.getOrders().size());
        assertEquals("Website design", result.getOrders().get(0).getDescription());
        verify(freelancerCache).clear();
    }

    @Test
    void addSkillToFreelancer_newSkill_addsSkill() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findByName("Java")).thenReturn(Optional.empty());
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancer);

        Freelancer result = freelancerService.addSkillToFreelancer(1L, "Java");

        assertNotNull(result.getSkills());
        assertEquals(1, result.getSkills().size());
        assertTrue(result.getSkills().contains(skill));
        verify(freelancerCache).clear();
    }

    @Test
    void deleteOrderFromFreelancer_valid_deletesOrder() {
        freelancer.getOrders().add(order);
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancer);

        freelancerService.deleteOrderFromFreelancer(1L, 1L);

        assertTrue(freelancer.getOrders().isEmpty());
        verify(orderRepository).delete(order);
        verify(freelancerCache).clear();
    }

    @Test
    void deleteOrderFromFreelancer_invalidOrder_throwsException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(ValidationException.class, () -> freelancerService.deleteOrderFromFreelancer(1L, 1L));
    }

    @Test
    void deleteSkillFromFreelancer_valid_deletesSkill() {
        freelancer.getSkills().add(skill);
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancer);

        freelancerService.deleteSkillFromFreelancer(1L, 1L);

        assertTrue(freelancer.getSkills().isEmpty());
        verify(freelancerCache).clear();
    }

    @Test
    void deleteSkillFromFreelancer_invalidSkill_throwsException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        assertThrows(ValidationException.class, () -> freelancerService.deleteSkillFromFreelancer(1L, 1L));
    }

    @Test
    void bulkUpsertFreelancers_valid_createsAndUpdates() {
        Freelancer newFreelancer = new Freelancer();
        newFreelancer.setName("Bob");
        newFreelancer.setCategory("dev");
        newFreelancer.setRating(4.0);
        newFreelancer.setHourlyRate(20.0);
        newFreelancer.setOrders(new ArrayList<>());
        newFreelancer.setSkills(new HashSet<>());

        Freelancer existingFreelancer = new Freelancer();
        existingFreelancer.setId(1L);
        existingFreelancer.setName("Alice Updated");
        existingFreelancer.setCategory("design");
        existingFreelancer.setRating(4.8);
        existingFreelancer.setHourlyRate(30.0);
        existingFreelancer.setOrders(new ArrayList<>());
        existingFreelancer.setSkills(new HashSet<>());

        List<Freelancer> input = List.of(newFreelancer, existingFreelancer);

        when(freelancerRepository.existsById(1L)).thenReturn(true);
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(input);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bob", result.get(0).getName());
        assertEquals("Alice Updated", result.get(1).getName());
        verify(freelancerCache).clear();
    }

    @Test
    void bulkUpsertFreelancers_emptyList_returnsEmpty() {
        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(List.of());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(freelancerCache, never()).clear();
    }

    @Test
    void bulkUpsertFreelancers_nullList_returnsEmpty() {
        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(freelancerCache, never()).clear();
    }

    @Test
    void bulkUpsertFreelancers_invalidEntries_filtersOut() {
        Freelancer validFreelancer = new Freelancer();
        validFreelancer.setName("Bob");
        validFreelancer.setCategory("dev");
        validFreelancer.setRating(4.0);
        validFreelancer.setHourlyRate(20.0);
        validFreelancer.setOrders(new ArrayList<>());
        validFreelancer.setSkills(new HashSet<>());

        Freelancer invalidFreelancer = new Freelancer();
        invalidFreelancer.setCategory("dev");
        invalidFreelancer.setOrders(new ArrayList<>());
        invalidFreelancer.setSkills(new HashSet<>());

        List<Freelancer> input = List.of(validFreelancer, invalidFreelancer, null);

        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(validFreelancer);

        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(input);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).getName());
        verify(freelancerCache).clear();
    }
}