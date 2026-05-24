package com.example.topfood2604.repository;

import com.example.topfood2604.entity.MemberDailyReportUsage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberDailyReportUsageRepository
        extends JpaRepository<MemberDailyReportUsage, Long> {

    Optional<MemberDailyReportUsage> findByMemberIdAndReportDate(
            Long memberId,
            LocalDate reportDate
    );

    @Query("""
        select coalesce(sum(u.reportCount), 0)
        from MemberDailyReportUsage u
        where u.reportDate = :reportDate
    """)
    Integer sumReportCountByReportDate(@Param("reportDate") LocalDate reportDate);
}