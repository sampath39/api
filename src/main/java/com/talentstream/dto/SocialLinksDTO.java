package com.talentstream.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotNull;


public class SocialLinksDTO {

	  private Long id;

	    @NotNull(message = "ApplicantId is required")
	    private Long applicantId;

	    @NotBlank(message = "GitHub URL is required")
	    @Pattern(
	        regexp = "^https://(www\\.)?github\\.com/[A-Za-z0-9_-]+/?$",
	        message = "Invalid GitHub URL"
	    )
	    private String github;
	    
	    @Pattern(
	        regexp = "^(https?:\\/\\/)?(www\\.)?linkedin\\.com\\/.*$",
	        message = "Invalid LinkedIn URL"
	    )
	    private String linkedIn;

	    @Pattern(
	        regexp = "^(https?:\\/\\/)?(www\\.)?leetcode\\.com\\/.*$",
	        message = "Invalid LeetCode URL"
	    )
	    private String leetcode;

	    
	    @Pattern(
	        regexp = "^(https?:\\/\\/)?(www\\.)?hackerrank\\.com\\/.*$",
	        message = "Invalid HackerRank URL"
	    )
	    private String hackerrank;

	    //  Getters

	    public Long getId() {
	        return id;
	    }

	    public Long getApplicantId() {
	        return applicantId;
	    }

	    public String getGithub() {
	        return github;
	    }

	    public String getLinkedIn() {
	        return linkedIn;
	    }

	    public String getLeetcode() {
	        return leetcode;
	    }

	    public String getHackerrank() {
	        return hackerrank;
	    }

	    // 🔹 Setters (with trim handling)

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public void setApplicantId(Long applicantId) {
	        this.applicantId = applicantId;
	    }

	    public void setGithub(String github) {
	        this.github = github != null ? github.trim() : null;
	    }

	    public void setLinkedIn(String linkedIn) {
	        this.linkedIn = linkedIn != null ? linkedIn.trim() : null;
	    }

	    public void setLeetcode(String leetcode) {
	        this.leetcode = leetcode != null ? leetcode.trim() : null;
	    }

	    public void setHackerrank(String hackerrank) {
	        this.hackerrank = hackerrank != null ? hackerrank.trim() : null;
	    }
}
