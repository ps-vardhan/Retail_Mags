package com.retail.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

public class InventoryDataDTO {
    private String productName;
    private Integer currentStock;
    private List<SalesHistoryDTO> salesHistory;

    public InventoryDataDTO() {}

    public InventoryDataDTO(String productName, Integer currentStock, List<SalesHistoryDTO> salesHistory) {
        this.productName = productName;
        this.currentStock = currentStock;
        this.salesHistory = salesHistory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }

    public List<SalesHistoryDTO> getSalesHistory() {
        return salesHistory;
    }

    public void setSalesHistory(List<SalesHistoryDTO> salesHistory) {
        this.salesHistory = salesHistory;
    }

    @Override
    public String toString() {
        return String.format("{Product: %s, Stock: %d, Sales: %s}", productName, currentStock, salesHistory);
    }
}

class SalesHistoryDTO {
    private LocalDateTime date;
    private Integer quantitySold;

    public SalesHistoryDTO() {}

    public SalesHistoryDTO(LocalDateTime date, Integer quantitySold) {
        this.date = date;
        this.quantitySold = quantitySold;
    }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    
    public Integer getQuantitySold() { return quantitySold; }
    public void setQuantitySold(Integer quantitySold) { this.quantitySold = quantitySold; }

    @Override
    public String toString() {
        return date.toLocalDate().toString() + ":" + quantitySold;
    }
}
