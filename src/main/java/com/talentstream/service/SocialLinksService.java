package com.talentstream.service;

import com.talentstream.dto.SocialLinksDTO;
import com.talentstream.entity.SocialLinks;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.SocialLinksRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class SocialLinksService {

    private static final Logger logger = LoggerFactory.getLogger(SocialLinksService.class);

    @Autowired
    private SocialLinksRepository repository;

    @Autowired
    private ApplicantRepository applicantRepository;

    // 🔹 CREATE
    public SocialLinksDTO createSocialLinks(SocialLinksDTO dto) {
    	  if (dto == null) {
    	        logger.error("SocialLinksDTO is null");
    	        throw new CustomException("Request body cannot be null", HttpStatus.BAD_REQUEST);
    	    }

        logger.info("Creating SocialLinks for applicantId={}", dto.getApplicantId());

        // ✅ Validate applicant
        applicantRepository.findById(dto.getApplicantId())
                .orElseThrow(() -> {
                    logger.error("Applicant not found: {}", dto.getApplicantId());
                    return new CustomException("Applicant not found", HttpStatus.NOT_FOUND);
                });

        // ✅ Check duplicate
        if (repository.existsByApplicantId(dto.getApplicantId())) {
            logger.warn("Duplicate SocialLinks attempt for applicantId={}", dto.getApplicantId());
            throw new CustomException(
                    "Social links already exist for this applicant",
                    HttpStatus.CONFLICT
            );
        }

        // ✅ Save
        SocialLinks saved = repository.save(mapToEntity(dto));

        logger.info("Successfully created SocialLinks for applicantId={}", dto.getApplicantId());

        return mapToDTO(saved);
    }

    // 🔹 GET
    public SocialLinksDTO getByApplicantId(Long applicantId) {
    	
    	if (applicantId == null) {
    	    logger.error("ApplicantId is null");
    	    throw new CustomException("ApplicantId is required", HttpStatus.BAD_REQUEST);
    	}

        logger.info("Fetching SocialLinks for applicantId={}", applicantId);

        // ✅ Validate applicant
        applicantRepository.findById(applicantId)
                .orElseThrow(() -> {
                    logger.error("Applicant not found: {}", applicantId);
                    return new CustomException("Applicant not found", HttpStatus.NOT_FOUND);
                });

        // ✅ Fetch data
        SocialLinks entity = repository.findByApplicantId(applicantId)
                .orElseThrow(() -> {
                    logger.warn("SocialLinks not found for applicantId={}", applicantId);
                    return new CustomException("Social links not found", HttpStatus.NOT_FOUND);
                });

        logger.info("Successfully fetched SocialLinks for applicantId={}", applicantId);

        return mapToDTO(entity);
    }

    // UPDATE
    public SocialLinksDTO updateSocialLinksByApplicantId(Long applicantId, SocialLinksDTO dto) {

        // ✅ Validations
        if (applicantId == null) {
            logger.error("Update failed. applicantId is null");
            throw new CustomException("ApplicantId is required", HttpStatus.BAD_REQUEST);
        }

        if (dto == null) {
            logger.error("Update failed. Request body is null for applicantId={}", applicantId);
            throw new CustomException("Request body cannot be null", HttpStatus.BAD_REQUEST);
        }

        logger.info("Updating SocialLinks for applicantId={}", applicantId);

        // ✅ Fetch existing record
        SocialLinks existing = repository.findByApplicantId(applicantId)
                .orElseThrow(() -> {
                    logger.error("SocialLinks not found for applicantId={}", applicantId);
                    return new CustomException("Social links not found", HttpStatus.NOT_FOUND);
                });

        // ✅ Update fields
        existing.setGithub(dto.getGithub());
        existing.setLinkedIn(dto.getLinkedIn());
        existing.setLeetcode(dto.getLeetcode());
        existing.setHackerrank(dto.getHackerrank());

        SocialLinks updated = repository.save(existing);

        logger.info("Successfully updated SocialLinks for applicantId={}", applicantId);

        return mapToDTO(updated);
    }
    //  DELETE
    public void deleteSocialLinksByApplicantId(Long applicantId) {

        if (applicantId == null) {
            logger.error("Delete failed. applicantId is null");
            throw new CustomException("ApplicantId is required", HttpStatus.BAD_REQUEST);
        }

        logger.info("Deleting SocialLinks for applicantId={}", applicantId);

        SocialLinks entity = repository.findByApplicantId(applicantId)
                .orElseThrow(() -> {
                    logger.error("Delete failed. SocialLinks not found for applicantId={}", applicantId);
                    return new CustomException("Social links not found", HttpStatus.NOT_FOUND);
                });

        repository.delete(entity);

        logger.info("Successfully deleted SocialLinks for applicantId={}", applicantId);
    }

    // 🔹 MAPPING: DTO → Entity
    private SocialLinks mapToEntity(SocialLinksDTO dto) {
        SocialLinks entity = new SocialLinks();

        entity.setApplicantId(dto.getApplicantId());
        entity.setGithub(dto.getGithub());
        entity.setLinkedIn(dto.getLinkedIn());
        entity.setLeetcode(dto.getLeetcode());
        entity.setHackerrank(dto.getHackerrank());

        return entity;
    }

    // 🔹 MAPPING: Entity → DTO
    private SocialLinksDTO mapToDTO(SocialLinks entity) {
        SocialLinksDTO dto = new SocialLinksDTO();

        dto.setId(entity.getId());
        dto.setApplicantId(entity.getApplicantId());
        dto.setGithub(entity.getGithub());
        dto.setLinkedIn(entity.getLinkedIn());
        dto.setLeetcode(entity.getLeetcode());
        dto.setHackerrank(entity.getHackerrank());

        return dto;
    }
}