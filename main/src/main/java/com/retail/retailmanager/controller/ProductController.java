package com.retail.retailmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.retail.retailmanager.repository.ProductRepository;
import com.retail.retailmanager.service.AIService;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final AIService aiService;

    public ProductController(ProductRepository productRepository,
            AIService aiService) {
        this.productRepository = productRepository;
        this.aiService = aiService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "products/list";
    }

    /**
     * Semantic search endpoint.
     * Embeds the query via OpenAI, searches pgvector for similar products,
     * returns the same list.html template populated with ranked results.
     *
     * Empty query falls back to full product listing to avoid
     * sending a blank string to the embedding model.
     */
    @GetMapping("/search")
    public String search(@RequestParam String q, Model model) {
        if (q == null || q.isBlank()) {
            model.addAttribute("products", productRepository.findAll());
        } else {
            model.addAttribute("products", aiService.semanticSearch(q));
            model.addAttribute("query", q);
        }
        return "products/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        productRepository.findById(id).ifPresent(p -> model.addAttribute("product", p));
        return "products/detail";
    }
}