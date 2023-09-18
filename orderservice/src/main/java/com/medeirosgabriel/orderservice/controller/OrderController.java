package com.medeirosgabriel.orderservice.controller;

import com.medeirosgabriel.orderservice.dto.ListOrdersDTO;
import com.medeirosgabriel.orderservice.enums.OrderStatus;
import com.medeirosgabriel.orderservice.model.Order_;
import com.medeirosgabriel.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/order")
@AllArgsConstructor
public class OrderController {

    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<Order_> createOrder() {
        Order_ order = new Order_(OrderStatus.PENDING);
        Order_ newOrder = this.orderRepository.save(order);
        return new ResponseEntity(newOrder, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping(value = "/{status}")
    public ResponseEntity<List<Order_>> listOrdersByStatus(@PathVariable("status") String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        List<Order_> orders = this.orderRepository.findByOrderStatus(orderStatus);
        return new ResponseEntity(orders, HttpStatus.OK);
    }
}
