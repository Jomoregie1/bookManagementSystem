package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.DatabaseAccessError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.OrderDto;
import com.bookstore.managementsystem.entity.Order;
import com.bookstore.managementsystem.repo.OrderRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<OrderDto> createOrder(OrderDto orderDto) throws DatabaseAccessError {
        Order order = mapConvertor.orderDtoToOrder(orderDto);
        try {
            orderRepo.save(order);
            return ResponseEntity.status(HttpStatus.CREATED).body(orderDto);
        } catch (DataAccessException e) {
            throw new DatabaseAccessError("Service is currently unavailable.");
        }
    }

    @Override
    public ResponseEntity<List<OrderDto>> getOrders() throws NotFoundError{
        List<Order> orders = orderRepo.findAll();
        if (orders.isEmpty()) {
            throw new NotFoundError("No orders found.");
        }

        List<OrderDto> orderDtos = orders.stream().map(mapConvertor::orderToOrderDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(orderDtos);
    }

    @Override
    public ResponseEntity<OrderDto> getOrder(Long id) throws NotFoundError {
        Optional<Order> order = orderRepo.findById(id);
        if (order.isEmpty()) {
            throw new NotFoundError("No orders found.");
        }
        OrderDto orderDto = order.map(mapConvertor::orderToOrderDto).get();

        return ResponseEntity.status(HttpStatus.OK).body(orderDto);
    }

    @Override
    public ResponseEntity<Void> updateOrder(Long id, OrderDto orderDto) throws NotFoundError{
        Optional<Order> order = orderRepo.findById(id);
        if (order.isEmpty()) {
            throw new NotFoundError("No orders found.");
        }
        Order newOrder = mapConvertor.orderDtoToOrder(orderDto);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    public ResponseEntity<Void> deleteOrder(Long id) {
        boolean exists = orderRepo.existsById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
