package com.example.topfood2604.repository;

import com.example.topfood2604.entity.MemberDailyLikeUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberDailyLikeUsageRepository
        extends JpaRepository<MemberDailyLikeUsage, Long> {

    Optional<MemberDailyLikeUsage> findByMemberIdAndLikeDate(
            Long memberId,
            LocalDate likeDate
    );
}