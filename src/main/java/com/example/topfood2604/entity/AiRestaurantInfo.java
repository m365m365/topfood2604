package com.example.topfood2604.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ai_restaurant_info")
public class AiRestaurantInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String url;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    @Column(name = "base64_image", columnDefinition = "LONGTEXT")
    private String base64Image;

    private String state;

    @Column(length = 500)
    private String address;

    @Column(name = "map_url", length = 1000)
    private String mapUrl;

    @Column(name = "embed_map_url", length = 1000)
    private String embedMapUrl;

    private Double lat;
    private Double lng;
}