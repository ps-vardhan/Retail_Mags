package com.retail.retailmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.retail.retailmanager.model.Inventory;
import com.retail.retailmanager.model.Product;
import com.retail.retailmanager.repository.InventoryRepository;
import com.retail.retailmanager.repository.OrderRepository;
import com.retail.retailmanager.repository.ProductRepository;
import com.retail.retailmanager.service.ProductSyncService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductSyncService productSyncService;

    public AdminController(ProductRepository productRepository,
            OrderRepository orderRepository,
            InventoryRepository inventoryRepository,
            ProductSyncService productSyncService) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.inventoryRepository = inventoryRepository;
        this.productSyncService = productSyncService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("orders", orderRepository.findAll());
        return "admin/dashboard";
    }

    /**
     * Triggers a full product sync from DummyJSON.
     * Replaces the dead saveSettings() method that was silently discarding
     * FlipkartConfig.
     * Redirects back to dashboard with a query param so Thymeleaf can show a
     * success banner.
     */
    @PostMapping("/sync")
    public String syncProducts() {
        productSyncService.syncAllProducts();
        return "redirect:/admin/dashboard?synced=true";
    }

    @PostMapping("/products/add")
    public String addProduct(@RequestParam String name,
            @RequestParam String description,
            @RequestParam java.math.BigDecimal price,
            @RequestParam String category,
            @RequestParam int quantity) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategory(category);
        productRepository.save(product);

        Inventory inventory = new Inventory();
        inventory.setProduct(product);
        inventory.setQuantity(quantity);
        inventoryRepository.save(inventory);

        return "redirect:/admin/dashboard";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        inventoryRepository.findByProductId(id)
                .ifPresent(inventoryRepository::delete);
        productRepository.deleteById(id);
        return "redirect:/admin/dashboard";
    }
}