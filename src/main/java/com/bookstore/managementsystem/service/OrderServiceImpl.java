package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.DatabaseAccessError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.OrderDto;
import com.bookstore.managementsystem.entity.Book;
import com.bookstore.managementsystem.entity.Order;
import com.bookstore.managementsystem.repo.BookRepo;
import com.bookstore.managementsystem.repo.OrderRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Qualifier("OrderService")
public class OrderServiceImpl implements OrderService{

    private OrderRepo orderRepo;
    private BookRepo bookRepo;
    private MapConvertor mapConvertor;

    @Autowired
    OrderServiceImpl(OrderRepo orderRepo, MapConvertor mapConvertor, BookRepo bookRepo){
        this.orderRepo = orderRepo;
        this.mapConvertor = mapConvertor;
        this.bookRepo = bookRepo;

    }

    @Override
    public ResponseEntity<OrderDto> createOrder(OrderDto orderDto) throws DatabaseAccessError {
        Order order = mapConvertor.orderDtoToOrder(orderDto);
        try {
            Set<Book> books = orderDto
                    .getBookTitles()
                    .stream()
                    .map(title -> {
                        try {
                            return bookRepo.findByTitle(title)
                                    .orElseThrow(() -> new NotFoundError("Book with title: " + title + " not found."));
                        } catch (NotFoundError e) {
                            throw new RuntimeException(e);
                        }
                    })
                            .collect(Collectors.toSet());


            books.forEach(order::addBook);
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
        orderRepo.save(newOrder);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Transactional
    @Override
    public ResponseEntity<Void> deleteOrder(Long id) throws NotFoundError {
        Optional<Order> optionalOrder = orderRepo.findById(id);
        if(optionalOrder.isEmpty()) {
            throw new NotFoundError("No orders found.");
        }
        Order order = optionalOrder.get();
        order.getBooks().forEach(order::removeBook);
        orderRepo.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
