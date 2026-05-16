package com.example.topfood2604.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(
        name = "member_daily_like_usage",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"member_id", "like_date"}
                )
        }
)
public class MemberDailyLikeUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "like_date")
    private LocalDate likeDate;

    @Column(name = "like_count")
    private Integer likeCount = 0;
}