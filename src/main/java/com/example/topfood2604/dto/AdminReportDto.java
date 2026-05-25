package com.example.topfood2604.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminReportDto {

    private Long id;
    private Long reporterMemberId;
    private Long reportedMemberId;
    private Long restaurantId;
    private String restaurantName;
    private LocalDateTime reportTime;
    private LocalDateTime blockedUntil;
    private Long countdownSeconds;
    private Boolean reviewPassed;
    private String status;
}