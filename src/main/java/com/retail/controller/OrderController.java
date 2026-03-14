package com.retail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.retail.service.OrderService;
import com.retail.service.ProductService;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    @GetMapping("/orders")
    public String viewOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "orders";
    }

    @GetMapping("/orderForm")
    public String showOrderForm(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "order_form";
    }
    
    @PostMapping("/placeOrder")
    public String placeOrder(@RequestParam String customerName, @RequestParam Long productId,
            @RequestParam Integer quantity) {
        try{
            orderService.placeOrder(customerName,productId,quantity);
            return "redirect:/orders";
        } catch (Exception e) {
            return "redirect:/orderForm?error=" + e.getMessage();
        }
    }
}
