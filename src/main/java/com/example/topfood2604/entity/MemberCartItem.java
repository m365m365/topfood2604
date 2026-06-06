package com.example.topfood2604.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "member_cart_item")
public class MemberCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Long productId;

    private Integer quantity;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Getter Setter
}