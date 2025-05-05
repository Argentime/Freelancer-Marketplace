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
    void createFreelancer_valid_savesFreelancer() {
        when(freelancerRepository.save(any(Freelancer.class))).thenReturn(freelancer);

        Freelancer result = freelancerService.createFreelancer(freelancer);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        assertNotNull(result.getOrders());
        assertNotNull(result.getSkills());
        verify(freelancerRepository).save(freelancer);
        verify(freelancerCache).clear();
    }

    @Test
    void createFreelancer_nullCollections_initializesCollections() {
        Freelancer input = new Freelancer();
        input.setName("Bob");
        input.setCategory("dev");
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Freelancer result = freelancerService.createFreelancer(input);

        assertNotNull(result.getOrders());
        assertTrue(result.getOrders().isEmpty());
        assertNotNull(result.getSkills());
        assertTrue(result.getSkills().isEmpty());
        verify(freelancerRepository).save(input);
        verify(freelancerCache).clear();
    }

    @Test
    void getFreelancerById_found_returnsFreelancer() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));

        Freelancer result = freelancerService.getFreelancerById(1L);

        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(freelancerRepository).findById(1L);
    }

    @Test
    void getFreelancerById_notFound_throwsNotFoundException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> freelancerService.getFreelancerById(1L));

        assertEquals("Freelancer with ID 1 not found", exception.getMessage());
        verify(freelancerRepository).findById(1L);
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
        assertEquals(4.8, result.getRating());
        assertEquals(30.0, result.getHourlyRate());
        verify(freelancerRepository).findById(1L);
        verify(freelancerRepository).save(freelancer);
        verify(freelancerCache).clear();
    }

    @Test
    void updateFreelancer_notFound_throwsNotFoundException() {
        Freelancer updatedDetails = new Freelancer();
        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> freelancerService.updateFreelancer(1L, updatedDetails));

        assertEquals("Freelancer with ID 1 not found", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void deleteFreelancer_exists_deletesFreelancer() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));

        freelancerService.deleteFreelancer(1L);

        verify(freelancerRepository).findById(1L);
        verify(freelancerRepository).deleteById(1L);
        verify(freelancerCache).clear();
    }

    @Test
    void deleteFreelancer_notFound_throwsNotFoundException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> freelancerService.deleteFreelancer(1L));

        assertEquals("Freelancer with ID 1 not found", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(freelancerRepository, never()).deleteById(any());
    }

    @Test
    void addOrderToFreelancer_valid_addsOrder() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Freelancer result = freelancerService.addOrderToFreelancer(1L, "Website design", 100.0);

        assertNotNull(result.getOrders());
        assertEquals(1, result.getOrders().size());
        assertEquals("Website design", result.getOrders().get(0).getDescription());
        assertEquals(100.0, result.getOrders().get(0).getPrice());
        assertEquals(freelancer, result.getOrders().get(0).getFreelancer());
        verify(freelancerRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
        verify(freelancerRepository).save(freelancer);
        verify(freelancerCache).clear();
    }

    @Test
    void addOrderToFreelancer_notFound_throwsNotFoundException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> freelancerService.addOrderToFreelancer(1L, "Website design", 100.0));

        assertEquals("Freelancer with ID 1 not found", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(orderRepository, never()).save(any());
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void addSkillToFreelancer_newSkill_addsSkill() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findByName("Java")).thenReturn(Optional.empty());
        when(skillRepository.save(any(Skill.class))).thenReturn(skill);
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Freelancer result = freelancerService.addSkillToFreelancer(1L, "Java");

        assertNotNull(result.getSkills());
        assertEquals(1, result.getSkills().size());
        assertTrue(result.getSkills().contains(skill));
        verify(freelancerRepository).findById(1L);
        verify(skillRepository).findByName("Java");
        verify(skillRepository).save(any(Skill.class));
        verify(freelancerRepository).save(freelancer);
        verify(freelancerCache).clear();
    }

    @Test
    void addSkillToFreelancer_existingSkill_addsSkill() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findByName("Java")).thenReturn(Optional.of(skill));
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Freelancer result = freelancerService.addSkillToFreelancer(1L, "Java");

        assertNotNull(result.getSkills());
        assertEquals(1, result.getSkills().size());
        assertTrue(result.getSkills().contains(skill));
        verify(freelancerRepository).findById(1L);
        verify(skillRepository).findByName("Java");
        verify(skillRepository, never()).save(any());
        verify(freelancerRepository).save(freelancer);
        verify(freelancerCache).clear();
    }

    @Test
    void addSkillToFreelancer_alreadyAssociated_throwsValidationException() {
        freelancer.getSkills().add(skill);
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findByName("Java")).thenReturn(Optional.of(skill));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> freelancerService.addSkillToFreelancer(1L, "Java"));

        assertEquals("Skill 'Java' is already associated with freelancer with ID 1", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(skillRepository).findByName("Java");
        verify(skillRepository, never()).save(any());
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void addSkillToFreelancer_notFound_throwsNotFoundException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> freelancerService.addSkillToFreelancer(1L, "Java"));

        assertEquals("Freelancer with ID 1 not found", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(skillRepository, never()).findByName(any());
    }

    @Test
    void deleteOrderFromFreelancer_valid_deletesOrder() {
        freelancer.getOrders().add(order);
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        freelancerService.deleteOrderFromFreelancer(1L, 1L);

        assertTrue(freelancer.getOrders().isEmpty());
        verify(freelancerRepository).findById(1L);
        verify(orderRepository).findById(1L);
        verify(orderRepository).delete(order);
        verify(freelancerRepository).save(freelancer);
        verify(freelancerCache).clear();
    }

    @Test
    void deleteOrderFromFreelancer_orderNotFound_throwsNotFoundException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> freelancerService.deleteOrderFromFreelancer(1L, 1L));

        assertEquals("Order with ID 1 not found", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).delete(any());
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void deleteOrderFromFreelancer_invalidOrder_throwsValidationException() {
        Order anotherOrder = new Order();
        anotherOrder.setId(2L);
        anotherOrder.setFreelancer(new Freelancer());
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(orderRepository.findById(2L)).thenReturn(Optional.of(anotherOrder));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> freelancerService.deleteOrderFromFreelancer(1L, 2L));

        assertEquals("Order with ID 2 does not belong to freelancer with ID 1", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(orderRepository).findById(2L);
        verify(orderRepository, never()).delete(any());
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void deleteOrderFromFreelancer_nullFreelancer_throwsValidationException() {
        Order anotherOrder = new Order();
        anotherOrder.setId(2L);
        anotherOrder.setFreelancer(null);
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(orderRepository.findById(2L)).thenReturn(Optional.of(anotherOrder));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> freelancerService.deleteOrderFromFreelancer(1L, 2L));

        assertEquals("Order with ID 2 does not belong to freelancer with ID 1", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(orderRepository).findById(2L);
        verify(orderRepository, never()).delete(any());
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void deleteSkillFromFreelancer_valid_deletesSkill() {
        freelancer.getSkills().add(skill);
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        freelancerService.deleteSkillFromFreelancer(1L, 1L);

        assertTrue(freelancer.getSkills().isEmpty());
        verify(freelancerRepository).findById(1L);
        verify(skillRepository).findById(1L);
        verify(freelancerRepository).save(freelancer);
        verify(freelancerCache).clear();
    }

    @Test
    void deleteSkillFromFreelancer_skillNotFound_throwsNotFoundException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> freelancerService.deleteSkillFromFreelancer(1L, 1L));

        assertEquals("Skill with ID 1 not found", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(skillRepository).findById(1L);
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void deleteSkillFromFreelancer_notAssociated_throwsValidationException() {
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(skillRepository.findById(1L)).thenReturn(Optional.of(skill));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> freelancerService.deleteSkillFromFreelancer(1L, 1L));

        assertEquals("Skill with ID 1 is not associated with freelancer with ID 1", exception.getMessage());
        verify(freelancerRepository).findById(1L);
        verify(skillRepository).findById(1L);
        verify(freelancerRepository, never()).save(any());
    }

    @Test
    void getFreelancers_cacheHit_returnsCachedSorted() {
        List<Freelancer> freelancers = new ArrayList<>();
        freelancers.add(freelancer);
        when(freelancerCache.containsKey(null, null)).thenReturn(true);
        when(freelancerCache.getFreelancers(null, null)).thenReturn(freelancers);

        List<Freelancer> result = freelancerService.getFreelancers(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(freelancerCache).containsKey(null, null);
        verify(freelancerCache).getFreelancers(null, null);
        verify(freelancerRepository, never()).findByCategoryAndSkill(any(), any());
    }

    @Test
    void getFreelancers_cacheMiss_returnsFromDbAndCaches() {
        List<Freelancer> freelancers = new ArrayList<>();
        freelancers.add(freelancer);
        when(freelancerCache.containsKey(null, null)).thenReturn(false);
        when(freelancerRepository.findByCategoryAndSkill(null, null)).thenReturn(freelancers);

        List<Freelancer> result = freelancerService.getFreelancers(null, null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(freelancerCache).containsKey(null, null);
        verify(freelancerRepository).findByCategoryAndSkill(null, null);
        verify(freelancerCache).putFreelancers(null, null, freelancers);
    }

    @Test
    void getFreelancers_withCategoryAndSkill_returnsSorted() {
        List<Freelancer> freelancers = new ArrayList<>();
        freelancers.add(freelancer);
        when(freelancerCache.containsKey("design", "Java")).thenReturn(false);
        when(freelancerRepository.findByCategoryAndSkill("design", "Java")).thenReturn(freelancers);

        List<Freelancer> result = freelancerService.getFreelancers("design", "Java");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).getName());
        verify(freelancerCache).containsKey("design", "Java");
        verify(freelancerRepository).findByCategoryAndSkill("design", "Java");
        verify(freelancerCache).putFreelancers("design", "Java", freelancers);
    }

    @Test
    void bulkUpsertFreelancers_valid_createsAndUpdates() {
        Freelancer newFreelancer = new Freelancer();
        newFreelancer.setName("Bob");
        newFreelancer.setCategory("dev");
        newFreelancer.setRating(4.0);
        newFreelancer.setHourlyRate(20.0);

        Freelancer existingFreelancer = new Freelancer();
        existingFreelancer.setId(1L);
        existingFreelancer.setName("Alice Updated");
        existingFreelancer.setCategory("design");
        existingFreelancer.setRating(4.8);
        existingFreelancer.setHourlyRate(30.0);

        List<Freelancer> input = new ArrayList<>();
        input.add(newFreelancer);
        input.add(existingFreelancer);

        when(freelancerRepository.existsById(1L)).thenReturn(true);
        when(freelancerRepository.findById(1L)).thenReturn(Optional.of(freelancer));
        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(input);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Bob", result.get(0).getName());
        assertEquals("Alice Updated", result.get(1).getName());
        verify(freelancerRepository, times(2)).save(any(Freelancer.class));
        verify(freelancerCache, times(2)).clear();
    }

    @Test
    void bulkUpsertFreelancers_emptyList_returnsEmpty() {
        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(new ArrayList<>());

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(freelancerRepository, never()).save(any());
        verify(freelancerCache, never()).clear();
    }

    @Test
    void bulkUpsertFreelancers_nullList_returnsEmpty() {
        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(freelancerRepository, never()).save(any());
        verify(freelancerCache, never()).clear();
    }

    @Test
    void bulkUpsertFreelancers_invalidEntries_filtersOut() {
        Freelancer validFreelancer = new Freelancer();
        validFreelancer.setName("Bob");
        validFreelancer.setCategory("dev");

        Freelancer invalidFreelancer = new Freelancer();
        invalidFreelancer.setCategory("dev");

        List<Freelancer> input = new ArrayList<>();
        input.add(validFreelancer);
        input.add(invalidFreelancer);
        input.add(null);

        when(freelancerRepository.save(any(Freelancer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Freelancer> result = freelancerService.bulkUpsertFreelancers(input);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bob", result.get(0).getName());
        verify(freelancerRepository).save(validFreelancer);
        verify(freelancerCache).clear();
    }
}