package com.example.topfood2604.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;


@Data
@Entity
@Table(name = "restaurant_report")
public class RestaurantReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reporterMemberId;

    private Long reportedMemberId;

    private Long restaurantId;

    private String reason;

    private LocalDateTime reportTime;

    private LocalDateTime blockedUntil;

    private Long countdownSeconds;

    private Boolean reviewPassed = false;

    private String status = "PENDING";

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.reportTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // getter / setter
}