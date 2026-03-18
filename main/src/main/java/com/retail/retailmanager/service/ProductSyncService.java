package com.retail.retailmanager.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.retail.retailmanager.model.Product;
import com.retail.retailmanager.repository.ProductRepository;

@Service
public class ProductSyncService {

    private final WebClient webClient;
    private final ProductRepository productRepository;
    private final EmbeddingService embeddingService;

    public ProductSyncService(
            @Value("${product.api.base-url}") String baseUrl,
            ProductRepository productRepository,
            EmbeddingService embeddingService) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        this.productRepository = productRepository;
        this.embeddingService = embeddingService;
    }

    /**
     * Fetches up to 100 products from DummyJSON, persists each one,
     * then immediately generates and stores its embedding vector.
     *
     * The embedding step is what feeds Pillar 3 — without it,
     * VectorStore.similaritySearch() returns zero results every time.
     */
    @SuppressWarnings("unchecked")
    public void syncAllProducts() {
        Map<String, Object> response = webClient.get()
                .uri("/products?limit=100&skip=0")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("products")) {
            throw new RuntimeException("DummyJSON returned an unexpected response shape");
        }

        List<Map<String, Object>> rawProducts = (List<Map<String, Object>>) response.get("products");

        for (Map<String, Object> raw : rawProducts) {
            Product product = new Product();
            product.setName((String) raw.get("title"));
            product.setDescription((String) raw.get("description"));
            product.setCategory((String) raw.get("category"));
            product.setThumbnailUrl((String) raw.get("thumbnail"));

            Number priceRaw = (Number) raw.get("price");
            product.setPrice(BigDecimal.valueOf(priceRaw.doubleValue()));

            // First save: persists the product and generates its DB id
            productRepository.save(product);

            // Second call: embeds name+description+category via OpenAI,
            // writes float[1536] to product.embedding, saves again.
            // This is what makes VectorStore.similaritySearch() return results.
            embeddingService.generateAndStore(product);
        }
    }
}