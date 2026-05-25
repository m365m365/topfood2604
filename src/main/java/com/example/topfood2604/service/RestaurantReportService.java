package com.example.topfood2604.service;

import com.example.topfood2604.dto.ReportResponseDto;
import com.example.topfood2604.dto.ReportStatusDto;
import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberDailyReportUsage;
import com.example.topfood2604.entity.MemberRecommendRestaurant;
import com.example.topfood2604.entity.RestaurantReport;
import com.example.topfood2604.repository.MemberDailyReportUsageRepository;
import com.example.topfood2604.repository.MemberRecommendRestaurantRepository;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.repository.RestaurantReportRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RestaurantReportService {

    private static final int GLOBAL_DAILY_REPORT_LIMIT = 25;

    private final RestaurantReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final MemberRecommendRestaurantRepository relationRepository;
    private final MemberDailyReportUsageRepository memberDailyReportUsageRepository;

    public RestaurantReportService(
            RestaurantReportRepository reportRepository,
            MemberRepository memberRepository,
            MemberRecommendRestaurantRepository relationRepository,
            MemberDailyReportUsageRepository memberDailyReportUsageRepository
    ) {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
        this.relationRepository = relationRepository;
        this.memberDailyReportUsageRepository = memberDailyReportUsageRepository;
    }

    @Transactional
    public ReportResponseDto reportRestaurant(
            Long restaurantId,
            Principal principal
    ) {

        if (principal == null) {
            return new ReportResponseDto(
                    true,
                    "GUEST_30_SECONDS",
                    "內容被檢舉，非會員效果版先遮蔽 15 秒，加入會員檢舉將遮蔽72小時審查",
                    15
            );
        }

        Member reporter = memberRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("找不到檢舉會員"));

        MemberRecommendRestaurant relation =
                relationRepository.findByRestaurant_Id(restaurantId)
                        .orElseThrow(() -> new RuntimeException("找不到餐廳推薦者"));

        Member reportedMember = relation.getMember();

        if (reportedMember.getTier() != null &&
                "PLATINUM".equalsIgnoreCase(reportedMember.getTier().getCode())) {

            return new ReportResponseDto(
                    true,
                    "PLATINUM_DENIED",
                    "白金會員 PLATINUM 無法檢舉，10 秒後恢復",
                    10
            );
        }

        LocalDate today = LocalDate.now();

        Integer totalToday =
                memberDailyReportUsageRepository.sumReportCountByReportDate(today);

        if (totalToday == null) {
            totalToday = 0;
        }

        if (totalToday >= GLOBAL_DAILY_REPORT_LIMIT) {
            throw new RuntimeException("今日全站檢舉次數已達上限 25 次，請明天再試。");
        }

        int dailyLimit = reporter.getTier().getDailyReportLimit();

        MemberDailyReportUsage usage =
                memberDailyReportUsageRepository
                        .findByMemberIdAndReportDate(reporter.getId(), today)
                        .orElseGet(() -> {
                            MemberDailyReportUsage u = new MemberDailyReportUsage();
                            u.setMemberId(reporter.getId());
                            u.setReportDate(today);
                            u.setReportCount(0);
                            return u;
                        });

        if (usage.getReportCount() >= dailyLimit) {
            throw new RuntimeException("你今日檢舉次數已達上限。");
        }

        Optional<RestaurantReport> existingReport =
                reportRepository.findTopByRestaurantIdAndStatusOrderByIdDesc(
                        restaurantId,
                        "ACTIVE"
                );

        if (existingReport.isPresent()) {

            RestaurantReport existing = existingReport.get();

            if (existing.getBlockedUntil() != null &&
                    existing.getBlockedUntil().isAfter(LocalDateTime.now())) {

                long remainingSeconds =
                        Duration.between(
                                LocalDateTime.now(),
                                existing.getBlockedUntil()
                        ).getSeconds();

                return new ReportResponseDto(
                        true,
                        "ALREADY_BLOCKED",
                        "此餐廳已在檢舉審查中",
                        remainingSeconds
                );
            }
        }

        RestaurantReport report = new RestaurantReport();

        report.setReporterMemberId(reporter.getId());
        report.setReportedMemberId(reportedMember.getId());
        report.setRestaurantId(restaurantId);
        report.setReason("會員檢舉不當內容");
        report.setReportTime(LocalDateTime.now());
        report.setBlockedUntil(LocalDateTime.now().plusHours(72));
        report.setCountdownSeconds(72L * 60L * 60L);
        report.setReviewPassed(false);
        report.setStatus("ACTIVE");

        reportRepository.save(report);

        usage.setReportCount(usage.getReportCount() + 1);
        memberDailyReportUsageRepository.save(usage);

        return new ReportResponseDto(
                true,
                "MEMBER_72_HOURS",
                "內容被會員檢舉，遮蔽 72 小時",
                72L * 60L * 60L
        );
    }

    public ReportStatusDto getTodayReportStatus(
            Authentication authentication
    ) {

        ReportStatusDto dto = new ReportStatusDto();

        LocalDate today = LocalDate.now();

        Integer globalUsed =
                memberDailyReportUsageRepository
                        .sumReportCountByReportDate(today);

        if (globalUsed == null) {
            globalUsed = 0;
        }

        dto.setGlobalUsed(globalUsed);
        dto.setGlobalLimit(GLOBAL_DAILY_REPORT_LIMIT);

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            dto.setCanReport(true);
            dto.setUsed(0);
            dto.setLimit(0);

            return dto;
        }

        Member member =
                memberRepository.findByUsername(authentication.getName())
                        .orElseThrow(() -> new RuntimeException("找不到會員"));

        int limit = member.getTier().getDailyReportLimit();

        MemberDailyReportUsage usage =
                memberDailyReportUsageRepository
                        .findByMemberIdAndReportDate(member.getId(), today)
                        .orElse(null);

        int used = 0;

        if (usage != null) {
            used = usage.getReportCount();
        }

        dto.setUsed(used);
        dto.setLimit(limit);

        boolean canReport =
                used < limit &&
                        globalUsed < GLOBAL_DAILY_REPORT_LIMIT;

        dto.setCanReport(canReport);

        return dto;
    }
}