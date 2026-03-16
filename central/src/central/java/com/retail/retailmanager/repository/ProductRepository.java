package com.retail.retailmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.retailmanager.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}