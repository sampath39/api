package com.talentstream.dto;

public class LeaderboardDTO {
	private Long applicantId;
    private String name;
    private Integer score;
    private Integer rank;

    // Constructor
    public LeaderboardDTO(String name, Integer score) {
        this.name = name;
        this.score = score;
    }

    // Getters
    public Long getApplicantId() {
        return applicantId;
    }

    public String getName() {
        return name;
    }

    public Integer getScore() {
        return score;
    }

    public Integer getRank() {
        return rank;
    }

    // Setters
    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

}
