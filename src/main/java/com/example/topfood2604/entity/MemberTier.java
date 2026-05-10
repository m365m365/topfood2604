package com.example.topfood2604.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "member_tier")
public class MemberTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="tier_name")
    private String tierName;

    @Column(name="daily_upload_limit")
    private Integer dailyUploadLimit;
}