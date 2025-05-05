package com.example.javalabs.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
    }

    @Test
    void testNoArgsConstructor() {
        Order o = new Order();
        assertNull(o.getId());
        assertNull(o.getDescription());
        assertEquals(0.0, o.getPrice());
        assertNull(o.getFreelancer());
    }

    @Test
    void testParameterizedConstructor() {
        Order o = new Order("Project work", 100.0);
        assertNull(o.getId());
        assertEquals("Project work", o.getDescription());
        assertEquals(100.0, o.getPrice());
        assertNull(o.getFreelancer());
    }

    @Test
    void testGettersAndSetters() {
        order.setId(1L);
        order.setDescription("Project work");
        order.setPrice(100.0);
        Freelancer freelancer = new Freelancer();
        order.setFreelancer(freelancer);

        assertEquals(1L, order.getId());
        assertEquals("Project work", order.getDescription());
        assertEquals(100.0, order.getPrice());
        assertSame(freelancer, order.getFreelancer());
    }

    @Test
    void testFreelancerRelationship() {
        Freelancer freelancer = new Freelancer();
        order.setFreelancer(freelancer);

        assertSame(freelancer, order.getFreelancer());
    }
}