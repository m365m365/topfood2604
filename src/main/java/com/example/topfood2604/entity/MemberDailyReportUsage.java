package com.example.topfood2604.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(
        name = "member_daily_report_usage",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "report_date"})
        }
)
public class MemberDailyReportUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Column(name = "report_count", nullable = false)
    private Integer reportCount = 0;

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getReportCount() {
        return reportCount;
    }

    public void setReportCount(Integer reportCount) {
        this.reportCount = reportCount;
    }
}