package com.example.topfood2604.dto;

public class MemberRankingDto {

    private Long memberId;
    private String username;

    private Long recommendCount;
    private Long likeCount;

    private String latestRestaurant;
    private String latestTime;

    public MemberRankingDto() {
    }

    public MemberRankingDto(
            Long memberId,
            String username,
            Long recommendCount,
            Long likeCount,
            String latestRestaurant,
            String latestTime
    ) {
        this.memberId = memberId;
        this.username = username;
        this.recommendCount = recommendCount;
        this.likeCount = likeCount;
        this.latestRestaurant = latestRestaurant;
        this.latestTime = latestTime;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(Long recommendCount) {
        this.recommendCount = recommendCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public String getLatestRestaurant() {
        return latestRestaurant;
    }

    public void setLatestRestaurant(String latestRestaurant) {
        this.latestRestaurant = latestRestaurant;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }
}