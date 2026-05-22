package com.example.topfood2604.service;

import com.example.topfood2604.dto.ReportResponseDto;
import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberRecommendRestaurant;
import com.example.topfood2604.entity.RestaurantReport;
import com.example.topfood2604.repository.MemberRecommendRestaurantRepository;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.repository.RestaurantReportRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class RestaurantReportService {

    private final RestaurantReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final MemberRecommendRestaurantRepository relationRepository;

    public RestaurantReportService(
            RestaurantReportRepository reportRepository,
            MemberRepository memberRepository,
            MemberRecommendRestaurantRepository relationRepository
    ) {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
        this.relationRepository = relationRepository;
    }

    @Transactional
    public ReportResponseDto reportRestaurant(
            Long restaurantId,
            Principal principal
    ) {

        // 1. 未登入者：只做前端 30 秒效果，不寫 DB
        if (principal == null) {
            return new ReportResponseDto(
                    true,
                    "GUEST_30_SECONDS",
                    "內容被檢舉，非會員效果版先遮蔽 15 秒,加入會員檢舉將遮蔽72小時審查",
                    15
            );
        }

        Member reporter = memberRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("找不到檢舉會員"));

        MemberRecommendRestaurant relation =
                relationRepository.findByRestaurant_Id(restaurantId)
                        .orElseThrow(() -> new RuntimeException("找不到餐廳推薦者"));

        Member reportedMember = relation.getMember();

        // 2. 白金會員不可檢舉
        if (reportedMember.getTier() != null &&
                "PLATINUM".equalsIgnoreCase(reportedMember.getTier().getCode())) {

            return new ReportResponseDto(
                    true,
                    "PLATINUM_DENIED",
                    "白金會員 PLATINUM 無法檢舉，10 秒後恢復",
                    10
            );
        }

        // 3. 已登入會員檢舉一般會員：寫入 DB，遮蔽 72 小時
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

        return new ReportResponseDto(
                true,
                "MEMBER_72_HOURS",
                "內容被會員檢舉，遮蔽 72 小時",
                72L * 60L * 60L
        );
    }
}