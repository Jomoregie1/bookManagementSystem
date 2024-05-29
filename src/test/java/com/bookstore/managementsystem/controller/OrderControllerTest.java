package com.bookstore.managementsystem.controller;

import com.bookstore.managementsystem.dto.OrderDto;
import com.bookstore.managementsystem.service.OrderService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private OrderDto orderDto;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        this.orderDto = OrderDto.builder()
                .date(LocalDate.of(2024, 10, 10))
                .totalAmount(20)
                .build();

        mapper = JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .build();

    }

    @Test
    public void testCreatingANewOrder_ThenReturnSuccess() throws Exception {
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        var response = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(this.orderDto)))
                .andReturn().getResponse();

        int status = response.getStatus();

        assertEquals(status, 201);
    }

    @Test
    public void testGetOrders_TheReturnListOfOrdersAndSuccessCode() throws Exception {
        when(orderService.getOrders())
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(List.of(this.orderDto, this.orderDto)));

        var response = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        int status = response.getStatus();
        List<OrderDto> orders = mapper.readValue(response.getContentAsString(), new TypeReference<List<OrderDto>>() {
        });

        assertEquals(status, 200);
        assertEquals(orders.size(), 2);


    }

    @Test
    public void testGetOrder_ThenReturnOrderAndSuccessCode() throws Exception {
        when(orderService.getOrder(any(Long.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(this.orderDto));

        var response = mockMvc.perform(get("/orders/1")).andReturn().getResponse();
        int status = response.getStatus();
        OrderDto orderDto1 = mapper.readValue(response.getContentAsString(), OrderDto.class);

        assertEquals(status, 200);
        assertEquals(orderDto1,this.orderDto);
    }

    @Test
    public void testUpdateOrder_ThenReturnSuccessCode() throws Exception {
        when(orderService.updateOrder(any(Long.class),any(OrderDto.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        var response = mockMvc.perform(put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(this.orderDto)))
                .andReturn().getResponse();

        int status_code = response.getStatus();

        assertEquals(status_code, 200);
    }

    @Test
    public void testDeleteOrder_ThenReturnSuccessCode() throws Exception {
        when(orderService.deleteOrder(any(Long.class))).thenReturn(ResponseEntity.status(HttpStatus.OK).build());

        var response = mockMvc.perform(delete("/orders/1")).andReturn().getResponse();
        int status_code = response.getStatus();

        assertEquals(status_code, 200);
    }
}