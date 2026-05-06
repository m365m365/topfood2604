package com.example.topfood2604.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private String name;

    private String email;

    private String role;

    @Column(name = "recommendID")
    private Long recommendID;

    private String state;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}