package com.example.topfood2604.repository;

import com.example.topfood2604.entity.GiftProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GiftProductRepository extends JpaRepository<GiftProduct, Long> {

    List<GiftProduct> findAllByOrderByCreatedAtDesc();

    List<GiftProduct> findByStatusOrderByCreatedAtDesc(String status);
}