package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.DatabaseAccessError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.dto.OrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    ResponseEntity<OrderDto> createOrder(OrderDto orderDto) throws DatabaseAccessError;

    ResponseEntity<List<OrderDto>> getOrders() throws NotFoundError;

    ResponseEntity<OrderDto> getOrder(Long id) throws NotFoundError;

    ResponseEntity<Void> updateOrder(Long id, OrderDto orderDto) throws NotFoundError;

    ResponseEntity<Void> deleteOrder(Long id);
}
