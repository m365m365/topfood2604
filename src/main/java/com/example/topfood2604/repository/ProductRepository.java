package com.example.topfood2604.repository;

import com.example.topfood2604.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStatusOrderBySortOrderAscIdAsc(String status);
}