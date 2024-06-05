package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.customerrors.DatabaseAccessError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.OrderDto;
import com.bookstore.managementsystem.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto) throws DatabaseAccessError {
        return orderService.createOrder(orderDto);
    }

    @GetMapping()
    public ResponseEntity<List<OrderDto>> getOrders() throws NotFoundError {
        return orderService.getOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable("id") Long id) throws NotFoundError {
        return orderService.getOrder(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOrder(@PathVariable("id") Long id, @RequestBody OrderDto orderDto) throws NotFoundError {
        return orderService.updateOrder(id, orderDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        return orderService.deleteOrder(id);
    }
}
