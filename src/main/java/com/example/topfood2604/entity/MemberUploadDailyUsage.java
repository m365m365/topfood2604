package com.example.topfood2604.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(
        name = "member_upload_daily_usage",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "upload_date"})
        }
)
public class MemberUploadDailyUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 哪個會員
     */
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * 哪一天
     */
    @Column(name = "upload_date", nullable = false)
    private LocalDate uploadDate;

    /**
     * 已使用次數
     */
    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }

        if (usedCount == null) {
            usedCount = 0;
        }
    }
}