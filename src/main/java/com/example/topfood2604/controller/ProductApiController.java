package com.example.topfood2604.controller;

import com.example.topfood2604.dto.ProductDto;
import com.example.topfood2604.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/products")
    public List<ProductDto> getProducts() {
        return productService.getActiveProducts();
    }
}