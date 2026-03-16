package com.retail.retailmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.retailmanager.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}