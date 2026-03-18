package com.retail.retailmanager.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.retail.retailmanager.model.Product;
import com.retail.retailmanager.repository.ProductRepository;

@Service
public class AIService {

    private final VectorStore vectorStore;
    private final EmbeddingModel embeddingModel;
    private final ProductRepository productRepository;

    public AIService(VectorStore vectorStore,
            EmbeddingModel embeddingModel,
            ProductRepository productRepository) {
        this.vectorStore = vectorStore;
        this.embeddingModel = embeddingModel;
        this.productRepository = productRepository;
    }

    /**
     * Embeds the query string, runs a cosine similarity search against pgvector,
     * then looks up the full Product entity for each result using the document
     * metadata ID.
     *
     * The metadata ID key is "product_id" — this must match what is stored
     * when documents are added to the VectorStore in EmbeddingService.
     *
     * topK=5 returns the five most semantically similar products.
     */
    public List<Product> semanticSearch(String query) {
        List<Double> queryVector = embeddingModel.embed(query);

        List<Document> docs = vectorStore.similaritySearch(
                SearchRequest.query(query).withTopK(5));

        return docs.stream()
                .map(doc -> {
                    Map<String, Object> metadata = doc.getMetadata();
                    Object rawId = metadata.get("product_id");
                    if (rawId == null)
                        return null;
                    Long productId = Long.valueOf(rawId.toString());
                    return productRepository.findById(productId).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}