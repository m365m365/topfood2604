package com.example.topfood2604.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "default_restaurant")
public class DefaultRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "map_url")
    private String mapUrl;

    private String description;

    @Column(name = "card_area")
    private String cardArea;

    @Column(name = "sort_order")
    private Integer sortOrder;

    private Boolean enabled;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}