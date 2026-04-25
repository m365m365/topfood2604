package com.example.topfood2604.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ai_restaurant_info")
public class AiRestaurantInfo2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    @Column(name = "image_url")
    private String imageUrl;

    @Lob
    @Column(name = "base64_image")
    private String base64Image;

    private String state;
}
