package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.OrderDto;
import com.bookstore.managementsystem.entity.Order;
import com.bookstore.managementsystem.repo.OrderRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("OrderService")
public class OrderServiceImpl implements OrderService{

    private OrderRepo orderRepo;
    private MapConvertor mapConvertor;

    @Autowired
    OrderServiceImpl(OrderRepo orderRepo, MapConvertor mapConvertor){
        this.orderRepo = orderRepo;
        this.mapConvertor = mapConvertor;
    }

    @Override
    public ResponseEntity<OrderDto> createOrder(OrderDto orderDto) {
        Order order = mapConvertor.orderDtoToOrder(orderDto);
        orderRepo.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
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
