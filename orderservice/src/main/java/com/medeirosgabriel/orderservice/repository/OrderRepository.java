package com.medeirosgabriel.orderservice.repository;

import com.medeirosgabriel.orderservice.enums.OrderStatus;
import com.medeirosgabriel.orderservice.model.Order_;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order_, Long> {

    String SKIP_LOCKED = "-2";
    @QueryHints(@QueryHint(name = AvailableSettings.JPA_LOCK_TIMEOUT, value = SKIP_LOCKED))
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Order_> findByOrderStatus(OrderStatus orderStatus);

}
