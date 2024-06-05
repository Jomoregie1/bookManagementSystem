package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.DatabaseAccessError;
import com.bookstore.managementsystem.dto.BookDto;
import com.bookstore.managementsystem.dto.OrderDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderService {

    ResponseEntity<OrderDto> createOrder(OrderDto orderDto) throws DatabaseAccessError;

    ResponseEntity<List<OrderDto>> getOrders();

    ResponseEntity<OrderDto> getOrder(Long id);

    ResponseEntity<Void> updateOrder(Long id, OrderDto orderDto);

    ResponseEntity<Void> deleteOrder(Long id);
}
