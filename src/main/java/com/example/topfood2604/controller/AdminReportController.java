package com.example.topfood2604.controller;

import com.example.topfood2604.dto.AdminReportDto;
import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.RestaurantReport;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.repository.RestaurantReportRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AdminReportController {

    private final RestaurantReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final JdbcTemplate jdbcTemplate;

    public AdminReportController(
            RestaurantReportRepository reportRepository,
            MemberRepository memberRepository,
            JdbcTemplate jdbcTemplate
    ) {
        this.reportRepository = reportRepository;
        this.memberRepository = memberRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/admin/reports")
    public String reports(Model model) {

        String sql = """
                SELECT
                    rp.id,
                    rp.reporter_member_id,
                    rp.reported_member_id,
                    rp.restaurant_id,
                    rr.restaurant_name,
                    rp.report_time,
                    rp.blocked_until,
                    rp.countdown_seconds,
                    rp.review_passed,
                    rp.status
                FROM restaurant_report rp
                LEFT JOIN recommended_restaurant rr
                    ON rr.id = rp.restaurant_id
                ORDER BY rp.report_time DESC
                """;

        List<AdminReportDto> reports = jdbcTemplate.query(sql, (rs, rowNum) -> {
            AdminReportDto dto = new AdminReportDto();

            dto.setId(rs.getLong("id"));
            dto.setReporterMemberId(rs.getLong("reporter_member_id"));
            dto.setReportedMemberId(rs.getLong("reported_member_id"));
            dto.setRestaurantId(rs.getLong("restaurant_id"));
            dto.setRestaurantName(rs.getString("restaurant_name"));

            if (rs.getTimestamp("report_time") != null) {
                dto.setReportTime(
                        rs.getTimestamp("report_time").toLocalDateTime()
                );
            }

            if (rs.getTimestamp("blocked_until") != null) {
                dto.setBlockedUntil(
                        rs.getTimestamp("blocked_until").toLocalDateTime()
                );
            }

            dto.setCountdownSeconds(rs.getLong("countdown_seconds"));
            dto.setReviewPassed(rs.getBoolean("review_passed"));
            dto.setStatus(rs.getString("status"));

            return dto;
        });

        model.addAttribute("reports", reports);

        return "admin/reports";
    }

    @PostMapping("/admin/reports/{id}/pass")
    public String passReport(@PathVariable Long id) {

        RestaurantReport report =
                reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("找不到檢舉"));

        report.setReviewPassed(true);
        report.setStatus("PASSED");
        report.setCountdownSeconds(0L);

        reportRepository.save(report);

        return "redirect:/admin/reports";
    }

    @PostMapping("/admin/reports/{id}/release")
    public String releaseReport(@PathVariable Long id) {

        RestaurantReport report =
                reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("找不到檢舉"));

        report.setStatus("RELEASED");
        report.setBlockedUntil(LocalDateTime.now().minusSeconds(1));
        report.setCountdownSeconds(0L);

        reportRepository.save(report);

        return "redirect:/admin/reports";
    }

    @PostMapping("/admin/reports/{id}/ban-member")
    public String banMember(@PathVariable Long id) {

        RestaurantReport report =
                reportRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("找不到檢舉"));

        Member member =
                memberRepository.findById(report.getReportedMemberId())
                        .orElseThrow(() -> new RuntimeException("找不到會員"));

        member.setState("DISABLED");

        memberRepository.save(member);

        report.setStatus("MEMBER_BANNED");
        report.setCountdownSeconds(0L);

        reportRepository.save(report);

        return "redirect:/admin/reports";
    }
}