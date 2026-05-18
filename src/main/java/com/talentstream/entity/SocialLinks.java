package com.talentstream.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;


@Entity
@Table(
    name = "social_links",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "applicant_id")
    }
)
public class SocialLinks {
	
	   @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    @Column(name = "applicant_id", nullable = false, unique = true)
	    private Long applicantId;

	    @Column(nullable = false)
	    private String github;

	    @Column(name = "linkedin")
	    private String linkedIn;

	    @Column
	    private String leetcode;

	    @Column
	    private String hackerrank;

	    @Column(name = "created_at", updatable = false)
	    private LocalDateTime createdAt;

	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;

	   // To set time stamps automatically
	    @PrePersist
	    public void prePersist() {
	        this.createdAt = LocalDateTime.now();
	    }

	    @PreUpdate
	    public void preUpdate() {
	        this.updatedAt = LocalDateTime.now();
	    }

	    //  Getters and Setters

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

	    public LocalDateTime getCreatedAt() {
	        return createdAt;
	    }

	    public LocalDateTime getUpdatedAt() {
	        return updatedAt;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public void setApplicantId(Long applicantId) {
	        this.applicantId = applicantId;
	    }

	    public void setGithub(String github) {
	        this.github = github;
	    }

	    public void setLinkedIn(String linkedIn) {
	        this.linkedIn = linkedIn;
	    }

	    public void setLeetcode(String leetcode) {
	        this.leetcode = leetcode;
	    }

	    public void setHackerrank(String hackerrank) {
	        this.hackerrank = hackerrank;
	    }

	    public void setCreatedAt(LocalDateTime createdAt) {
	        this.createdAt = createdAt;
	    }

	    public void setUpdatedAt(LocalDateTime updatedAt) {
	        this.updatedAt = updatedAt;
	    }

}
