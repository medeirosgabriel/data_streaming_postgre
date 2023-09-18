package com.medeirosgabriel.orderservice.model;

import com.medeirosgabriel.orderservice.enums.OrderStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Order_ {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    private OrderStatus orderStatus;

    public Order_(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}
