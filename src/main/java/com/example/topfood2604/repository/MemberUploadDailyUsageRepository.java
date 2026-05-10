package com.example.topfood2604.repository;

import com.example.topfood2604.entity.MemberUploadDailyUsage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MemberUploadDailyUsageRepository
        extends JpaRepository<MemberUploadDailyUsage, Long> {

    List<MemberUploadDailyUsage> findAllByOrderByUploadDateDescIdDesc();

    List<MemberUploadDailyUsage> findByUploadDateOrderByIdDesc(LocalDate uploadDate);

    Optional<MemberUploadDailyUsage> findByMemberIdAndUploadDate(
            Long memberId,
            LocalDate uploadDate
    );
}