package com.example.javalabs.repositories;

import com.example.javalabs.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}