package com.example.topfood2604.entity;

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

    @Column(unique = true)
    private String username;

    private String password;

    private String name;

    private String email;

    private String role;

    private String state;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 會員推薦的餐廳關聯
     */
    @OneToMany(mappedBy = "member")
    private List<MemberRecommendRestaurant> recommendRestaurants;
}