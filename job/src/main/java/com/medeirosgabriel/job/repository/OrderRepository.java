package com.medeirosgabriel.job.repository;

import com.medeirosgabriel.job.model.Order_;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order_, Long> { }
