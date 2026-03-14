package com.retail.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.model.Inventory;
import com.retail.model.Product;
import com.retail.repository.InventoryRepository;
import com.retail.repository.ProductRepository;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private ProductRepository productRepo;

    public List<Inventory> getAllInventory() {
        return inventoryRepo.findAll();
    }

    public void addStock(Long productId, Integer quantity, String location) {
        Inventory inventory = inventoryRepo.findByProductId(productId);
        if (inventory == null) {
            inventory = new Inventory();
            Product p = productRepo.findById(productId).orElseThrow();
            inventory.setProduct(p);
        }
        inventory.setQuantity(quantity);
        inventory.setLocation(location);
        inventoryRepo.save(inventory);
    }
}
