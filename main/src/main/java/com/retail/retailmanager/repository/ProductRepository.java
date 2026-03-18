package com.retail.retailmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.retailmanager.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Used by ProductSyncService to look up an existing product before
     * creating a new one. Without this, every sync run inserts duplicate
     * rows because save() is called unconditionally on a transient Product.
     *
     * The lookup key is the product title from DummyJSON, mapped to
     * product.name. Not a globally unique natural key, but sufficient
     * to prevent duplicates across repeated syncs of the same dataset.
     */
    Optional<Product> findByName(String name);
}