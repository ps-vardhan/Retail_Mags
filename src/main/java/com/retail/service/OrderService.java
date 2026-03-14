package com.retail.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.retail.model.Inventory;
import com.retail.model.Order;
import com.retail.model.OrderItem;
import com.retail.model.Product;
import com.retail.repository.InventoryRepository;
import com.retail.repository.OrderRepository;
import com.retail.repository.ProductRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private InventoryRepository inventoryRepo;
    
    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    @Transactional
    public void placeOrder(String customerName, Long productId, Integer quantity) {
        Order order = new Order();
        order.setCustomerName(customerName);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("COMPLETED");

        Inventory inventory = inventoryRepo.findByProductId(productId);
        if (inventory == null || inventory.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient Stock!");
        }
        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepo.save(inventory);

        Product product = productRepo.findById(productId).orElseThrow();
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(quantity);

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setItems(items);

        orderRepo.save(order);
  }
}
