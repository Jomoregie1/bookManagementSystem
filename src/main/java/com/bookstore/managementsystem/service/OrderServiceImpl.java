package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.OrderDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("OrderService")
public class OrderServiceImpl implements OrderService{

    @Override
    public ResponseEntity<Void> createOrder(OrderDto orderDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<OrderDto>> getOrders() {
        return null;
    }

    @Override
    public ResponseEntity<OrderDto> getOrder(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateOrder(Long id, OrderDto orderDto) {
        return null;
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        return null;
    }
}
