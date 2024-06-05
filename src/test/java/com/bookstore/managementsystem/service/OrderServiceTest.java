package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.customerrors.DatabaseAccessError;
import com.bookstore.managementsystem.customerrors.NotFoundError;
import com.bookstore.managementsystem.dto.OrderDto;
import com.bookstore.managementsystem.entity.Order;
import com.bookstore.managementsystem.repo.OrderRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepo orderRepo;

    @Mock
    private MapConvertor mapConvertor;
    private OrderDto orderDto;
    private Order order;

    @BeforeEach
    void setUp(){
        this.orderDto = OrderDto.builder()
                .date(LocalDate.of(2024, 11, 15))
                .totalAmount(10)
                .build();

        this.order = Order.builder()
                .orderDate(this.orderDto.getDate())
                .totalAmount(this.orderDto.getTotalAmount())
                .id(1L)
                .build();

    }

    @Test
    public void testCreateOrder_WhenValidOrderPassed_ThenReturn201() throws DatabaseAccessError {
        when(mapConvertor.orderDtoToOrder(any(OrderDto.class))).thenReturn(this.order);

        ResponseEntity<OrderDto> orderDtoResponseEntity = orderService.createOrder(this.orderDto);
        int statusCode = orderDtoResponseEntity.getStatusCode().value();
        OrderDto orderDtoResponse = orderDtoResponseEntity.getBody();

        assertEquals(201, statusCode);
        assertEquals(orderDtoResponse, this.orderDto);

    }

    @Test
    public void testCreateOrder_WhenOrderFailsToSaveToTheDatabase_ThenThrowDataAccessException() throws DatabaseAccessError{
        when(mapConvertor.orderDtoToOrder(any(OrderDto.class))).thenReturn(this.order);
        when(orderRepo.save(any(Order.class))).thenThrow(new DataAccessException("...") {});

        DatabaseAccessError thrownError =
                assertThrows(DatabaseAccessError.class, () -> {orderService.createOrder(this.orderDto);});

    }

    @Test
    public void testGetOrders_WhenOrdersPresentInTheData_ThenReturnAListOfOrders() throws NotFoundError{
        when(orderRepo.findAll()).thenReturn(List.of(this.order,this.order,this.order));
        when(mapConvertor.orderToOrderDto(any(Order.class))).thenReturn(this.orderDto);

        ResponseEntity<List<OrderDto>> response = orderService.getOrders();
        int statusCode = response.getStatusCode().value();
        List<OrderDto> orders = response.getBody();

        assertEquals(200,statusCode);
        assertEquals(3, orders.size());
        assertEquals(this.orderDto, orders.get(0));
    }

    @Test
    public void testGetOrders_WhenListOfOrdersIsEmpty_ThenReturnNotFoundError(){
        when(orderRepo.findAll()).thenReturn(List.of());
        NotFoundError raisedError = assertThrows(NotFoundError.class, () -> {orderService.getOrders();});
        assertEquals("No orders found.", raisedError.getMessage());
    }

    @Test
    public void testGetOrder_WhenValidOrderIdGiven_ThenReturnOrder() {
        when(orderRepo.findById(any(Long.class))).thenReturn(Optional.of(this.order));
        when(mapConvertor.orderToOrderDto(any(Order.class))).thenReturn(this.orderDto);
        long testId = 1L;


        ResponseEntity<OrderDto> response = orderService.getOrder(testId);
        int statusCode = response.getStatusCode().value();
        OrderDto orderDto = response.getBody();

        assertEquals(200,statusCode);
        assertEquals(this.orderDto, orderDto);
    }

    @Test
    public void testGetOrder_WhenInvalidOrderIdIsGiven_ThenThrowNotFoundError(){
        when(orderRepo.findById(any(Long.class))).thenReturn(Optional.empty());
        long testId = 1L;

        NotFoundError raisedError = assertThrows(NotFoundError.class, () -> {orderService.getOrder(testId);});
        assertEquals("No orders found.", raisedError.getMessage());
        
    }


}