package com.bookstore.managementsystem.service;

import com.bookstore.managementsystem.dto.OrderDto;
import com.bookstore.managementsystem.entity.Order;
import com.bookstore.managementsystem.repo.OrderRepo;
import com.bookstore.managementsystem.utils.MapConvertor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

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
    public void testCreateOrder_whenValidOrderPassed_Then





}