package com.example.topfood2604.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "recommended_restaurant")
public class RecommendedRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="restaurant_name")
    private String restaurantName;

    private String address;

    @Column(name="image_url")
    private String imageUrl;

    @Column(name="thumb_url")
    private String thumbUrl;

    @Column(name="star_rating")
    private Integer starRating;

    private String description;

    @Column(name="map_url")
    private String mapUrl;

    private String status;

    @Column(name="created_at")
    private LocalDateTime createdAt;
}