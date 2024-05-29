package com.bookstore.managementsystem.repo;

import com.bookstore.managementsystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
}
