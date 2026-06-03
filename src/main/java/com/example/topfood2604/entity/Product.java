package com.example.topfood2604.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private String description;
    private Integer price;
    private Integer stock;
    private String imageUrl;
    private String category;
    private String status;
    private Integer sortOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public Integer getPrice() { return price; }
    public Integer getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
    public String getStatus() { return status; }
    public Integer getSortOrder() { return sortOrder; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setProductName(String productName) { this.productName = productName; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(Integer price) { this.price = price; }
    public void setStock(Integer stock) { this.stock = stock; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setCategory(String category) { this.category = category; }
    public void setStatus(String status) { this.status = status; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}