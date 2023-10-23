package com.medeirosgabriel.job.controller;

import com.medeirosgabriel.job.SendMessageService;
import com.medeirosgabriel.job.enums.OrderStatus;
import com.medeirosgabriel.job.model.Order_;
import com.medeirosgabriel.job.repository.OrderRepository;
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

    @Autowired
    private SendMessageService<Order_> orderSendMessageService;

    @PostMapping
    public ResponseEntity<Order_> createOrder() {
        Order_ order = new Order_(OrderStatus.PENDING);
        order.setOrderStatus(OrderStatus.COMPLETED);
        orderSendMessageService.sendMessage(order);
        return new ResponseEntity(order, HttpStatus.CREATED);
    }

    @Transactional
    @GetMapping(value = "/{status}")
    public ResponseEntity<List<Order_>> listOrdersByStatus(@PathVariable("status") String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status);
        List<Order_> orders = this.orderRepository.findByOrderStatus(orderStatus);
        return new ResponseEntity(orders, HttpStatus.OK);
    }
}
