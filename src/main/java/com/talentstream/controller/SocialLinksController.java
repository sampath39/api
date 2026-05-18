package com.talentstream.controller;

import com.talentstream.dto.SocialLinksDTO;
import com.talentstream.exception.CustomException;
import com.talentstream.service.SocialLinksService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/social-links")

public class SocialLinksController {

    private static final Logger logger = LoggerFactory.getLogger(SocialLinksController.class);

    @Autowired
    private SocialLinksService service;

    // CREATE
    @PostMapping
    public ResponseEntity<SocialLinksDTO> createSocialLinks(
            @Valid @RequestBody SocialLinksDTO dto) {

        logger.info("Request: Create SocialLinks for applicantId={}", dto.getApplicantId());

        SocialLinksDTO response = service.createSocialLinks(dto);

        logger.info("Response: SocialLinks created for applicantId={}", response.getApplicantId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ GET
    @GetMapping("/{applicantId}")
    public ResponseEntity<SocialLinksDTO> getSocialLinksByApplicantId(
            @PathVariable Long applicantId) {

        if (applicantId == null) {
            logger.error("Get request failed: applicantId is null");
            throw new CustomException("ApplicantId is required", HttpStatus.BAD_REQUEST);
        }

        logger.info("Request: Get SocialLinks for applicantId={}", applicantId);

        SocialLinksDTO response = service.getByApplicantId(applicantId);

        logger.info("Response: SocialLinks fetched for applicantId={}", applicantId);

        return ResponseEntity.ok(response);
    }

    // ✅ UPDATE (Using applicantId instead of id)
    @PutMapping("/{applicantId}")
    public ResponseEntity<SocialLinksDTO> updateSocialLinks(
            @PathVariable Long applicantId,
            @Valid @RequestBody SocialLinksDTO dto) {

        if (applicantId == null) {
            logger.error("Update request failed: applicantId is null");
            throw new CustomException("ApplicantId is required", HttpStatus.BAD_REQUEST);
        }

        if (dto == null) {
            logger.error("Update request failed: Request body is null for applicantId={}", applicantId);
            throw new CustomException("Request body cannot be null", HttpStatus.BAD_REQUEST);
        }

        logger.info("Request: Update SocialLinks for applicantId={}", applicantId);

        SocialLinksDTO response = service.updateSocialLinksByApplicantId(applicantId, dto);

        logger.info("Response: SocialLinks updated for applicantId={}", applicantId);

        return ResponseEntity.ok(response);
    }

    // ✅ DELETE (Using applicantId)
    @DeleteMapping("/{applicantId}")
    public ResponseEntity<Void> deleteSocialLinks(
            @PathVariable Long applicantId) {

        if (applicantId == null) {
            logger.error("Delete request failed: applicantId is null");
            throw new CustomException("ApplicantId is required", HttpStatus.BAD_REQUEST);
        }

        logger.info("Request: Delete SocialLinks for applicantId={}", applicantId);

        service.deleteSocialLinksByApplicantId(applicantId);

        logger.info("Response: SocialLinks deleted for applicantId={}", applicantId);

        return ResponseEntity.noContent().build(); // 204
    }
}