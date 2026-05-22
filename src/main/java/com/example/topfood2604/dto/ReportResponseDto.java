package com.example.topfood2604.dto;

public class ReportResponseDto {

    private boolean success;
    private String action;
    private String message;
    private long countdownSeconds;

    public ReportResponseDto(
            boolean success,
            String action,
            String message,
            long countdownSeconds
    ) {
        this.success = success;
        this.action = action;
        this.message = message;
        this.countdownSeconds = countdownSeconds;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAction() {
        return action;
    }

    public String getMessage() {
        return message;
    }

    public long getCountdownSeconds() {
        return countdownSeconds;
    }
}