package com.example.topfood2604.service;

import com.example.topfood2604.dto.ProductDto;
import com.example.topfood2604.entity.Product;
import com.example.topfood2604.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getActiveProducts() {

        List<Product> products =
                productRepository.findByStatusOrderBySortOrderAscIdAsc("ACTIVE");

        return products.stream()
                .map(p -> new ProductDto(
                        p.getId(),
                        p.getProductName(),
                        p.getDescription(),
                        p.getPrice(),
                        p.getStock(),
                        p.getImageUrl(),
                        p.getCategory()
                ))
                .toList();
    }
}