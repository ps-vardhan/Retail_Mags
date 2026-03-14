package com.retail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.retail.service.InventoryService;
import com.retail.service.ProductService;

@Controller
public class InventoryController {
    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/inventory")
    public String viewInventory(Model model) {
        model.addAttribute("inventoryList", inventoryService.getAllInventory());
        model.addAttribute("products", productService.getAllProducts());
        return "inventory";

    }

    @PostMapping("/updateInventory")
    public String updateInventory(@RequestParam Long productId, @RequestParam Integer quantity,
            @RequestParam String location) {
        inventoryService.addStock(productId, quantity, location);
        return "redirect:/inventory";
    }
}
