package com.medeirosgabriel.orderservice.dto;

import com.medeirosgabriel.orderservice.enums.OrderStatus;

public class ListOrdersDTO {
    private OrderStatus orderStatus;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
