package com.example.topfood2604.dto;

public class ProductDto {

    private Long id;
    private String productName;
    private String description;
    private Integer price;
    private Integer stock;
    private String imageUrl;
    private String category;

    public ProductDto(Long id, String productName, String description,
                      Integer price, Integer stock, String imageUrl, String category) {
        this.id = id;
        this.productName = productName;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() { return id; }
    public String getProductName() { return productName; }
    public String getDescription() { return description; }
    public Integer getPrice() { return price; }
    public Integer getStock() { return stock; }
    public String getImageUrl() { return imageUrl; }
    public String getCategory() { return category; }
}