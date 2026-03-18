package com.retail.retailmanager.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.retail.retailmanager.model.Inventory;
import com.retail.retailmanager.model.Order;
import com.retail.retailmanager.model.Product;
import com.retail.retailmanager.model.User;
import com.retail.retailmanager.repository.InventoryRepository;
import com.retail.retailmanager.repository.OrderRepository;
import com.retail.retailmanager.repository.ProductRepository;
import com.retail.retailmanager.repository.UserRepository;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    public OrderController(OrderRepository orderRepository,
            ProductRepository productRepository,
            InventoryRepository inventoryRepository,
            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    @Transactional
    public String placeOrder(@RequestParam Long productId,
            @RequestParam int quantity,
            @AuthenticationPrincipal UserDetails userDetails) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (inventory.getQuantity() < quantity) {
            return "redirect:/products?error=insufficient_stock";
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        inventoryRepository.save(inventory);

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setQuantity(quantity);
        orderRepository.save(order);

        return "redirect:/products?success=order_placed";
    }
}