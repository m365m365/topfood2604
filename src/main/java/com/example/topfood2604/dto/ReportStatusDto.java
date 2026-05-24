package com.example.topfood2604.dto;

public class ReportStatusDto {

    private boolean canReport;

    private int used;

    private int limit;

    private int globalUsed;

    private int globalLimit;

    public boolean isCanReport() {
        return canReport;
    }

    public void setCanReport(boolean canReport) {
        this.canReport = canReport;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getGlobalUsed() {
        return globalUsed;
    }

    public void setGlobalUsed(int globalUsed) {
        this.globalUsed = globalUsed;
    }

    public int getGlobalLimit() {
        return globalLimit;
    }

    public void setGlobalLimit(int globalLimit) {
        this.globalLimit = globalLimit;
    }
}