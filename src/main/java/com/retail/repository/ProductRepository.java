package com.retail.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.retail.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    
}
