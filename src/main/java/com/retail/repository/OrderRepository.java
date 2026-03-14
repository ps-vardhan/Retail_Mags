package com.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    
}
