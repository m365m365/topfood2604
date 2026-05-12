package com.example.topfood2604.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    private String email;

    /**
     * USER / ADMIN
     */
    private String role;

    /**
     * ACTIVE / DISABLED
     */
    private String state;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 會員等級
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id")
    private MemberTier tier;



    @Column(nullable = false)
    private Boolean emailVerified = false;

    @Column(length = 255)
    private String verificationToken;

    private LocalDateTime verificationTokenExpiredAt;
    /**
     * 使用者推薦過的餐廳
     */
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<MemberRecommendRestaurant> recommendRestaurants;

    @PrePersist
    public void prePersist() {

        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (role == null) {
            role = "USER";
        }

        if (state == null) {
            state = "ACTIVE";
        }
    }
}