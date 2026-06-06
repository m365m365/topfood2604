package com.example.topfood2604.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {

    private Long productId;

    private String productName;

    private Integer price;

    private String imageUrl;

    private Integer stock;

    private Integer quantity;

    private Integer subtotal;
}