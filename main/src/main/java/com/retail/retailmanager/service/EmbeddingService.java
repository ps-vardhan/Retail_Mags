package com.retail.retailmanager.service;

import java.util.List;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import com.retail.retailmanager.model.Product;
import com.retail.retailmanager.repository.ProductRepository;

@Service
public class EmbeddingService {

    private final EmbeddingModel embeddingModel;
    private final ProductRepository productRepository;

    public EmbeddingService(EmbeddingModel embeddingModel,
            ProductRepository productRepository) {
        this.embeddingModel = embeddingModel;
        this.productRepository = productRepository;
    }

    /**
     * Builds a text blob from the product's name, description, and category,
     * calls OpenAI text-embedding-ada-002 via EmbeddingModel,
     * converts the returned List<Double> to float[],
     * and writes it back to product.embedding via the repository.
     *
     * Called by ProductSyncService after every productRepository.save().
     */
    public void generateAndStore(Product product) {
        String text = buildEmbeddingText(product);

        // EmbeddingModel.embed() returns List<Double> of length 1536 for ada-002
        List<Double> vector = embeddingModel.embed(text);

        float[] embedding = new float[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            embedding[i] = vector.get(i).floatValue();
        }

        product.setEmbedding(embedding);
        productRepository.save(product);
    }

    /**
     * Constructs the text passed to the embedding model.
     * Richer text = better semantic search results.
     * Null-safe: skips fields that are null to avoid "null" literal in the string.
     */
    private String buildEmbeddingText(Product product) {
        StringBuilder sb = new StringBuilder();
        if (product.getName() != null) {
            sb.append(product.getName()).append(". ");
        }
        if (product.getDescription() != null) {
            sb.append(product.getDescription()).append(". ");
        }
        if (product.getCategory() != null) {
            sb.append("Category: ").append(product.getCategory());
        }
        return sb.toString().trim();
    }
}