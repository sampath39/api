package com.talentstream.ats;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talentstream.dto.ApplicantFullDataDTO;
import com.talentstream.dto.ResumeSchemaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class SchemaFormatter {
	private static final Logger logger = LoggerFactory.getLogger(SchemaFormatter.class);
	private String safeGet(Map<String, Object> map, String key) {
	    Object value = map.get(key);
	    return value != null ? value.toString() : "";
	}
	public ResumeSchemaDTO format(ApplicantFullDataDTO dto) {
		System.out.println("SkillBadgesJson: " + dto.getSkillBadgesJson());
		System.out.println("SocialLinksJson: " + dto.getSocialLinksJson());

		// ===== HEADER =====
		Map<String, String> social = extractSocialLinksMap(dto.getSocialLinksJson());

		StringBuilder header = new StringBuilder();

		header.append(dto.getFirstName())
		      .append(" | ")
		      .append(dto.getTitle())
		      .append(" | ")
		      .append(dto.getEmail())
		      .append(" | ")
		      .append(dto.getAlternatePhoneNumber());

		appendSocialLink(header, "LinkedIn", social.get("linkedin"));
		appendSocialLink(header, "GitHub", social.get("github"));
		appendSocialLink(header, "LeetCode", social.get("leetcode"));
		appendSocialLink(header, "HackerRank", social.get("hackerrank"));

		ResumeSchemaDTO resume = new ResumeSchemaDTO();
		resume.setHeader(header.toString());

		List<ResumeSchemaDTO.Section> sections = new ArrayList<>();

		// ===== SUMMARY =====
		if (dto.getSummary() != null && !dto.getSummary().isBlank()) {
			sections.add(section("SUMMARY", List.of(esc(dto.getSummary()))));
		}
		// ===== SKILLS =====
		if (dto.getSkillsJson() != null && !dto.getSkillsJson().isBlank()) {
			sections.add(section("SKILLS", extractKeyWords(dto.getSkillsJson())));

		}
		
		// ===== SKILL BADGES =====
		if (dto.getSkillBadgesJson() != null && !dto.getSkillBadgesJson().isBlank()) {
		    sections.add(section(
		        "SKILL BADGES",
		        extractKeyWords(dto.getSkillBadgesJson())
		    ));
		}
		
		// ===== SOCIAL LINKS =====
//		if (dto.getSocialLinksJson() != null && !dto.getSocialLinksJson().isBlank()) {
//		    sections.add(section(
//		        "SOCIAL LINKS",
//		        extractSocialLinks(dto.getSocialLinksJson())
//		    ));
//		}
		
		

		// ===== SKILLS FROM PROJECT JSON =====
		List<String> skills = extractSkillsFromProjects(esc(dto.getProjectsJson()));
		if (!skills.isEmpty()) {
			sections.add(section("SKILLS", skills));
		}
		// ===== PROJECTS =====
		List<String> projectLines = extractProjectLines(dto.getProjectsJson());
		if (!projectLines.isEmpty()) {
			sections.add(section("PROJECTS", projectLines));
		}

		// ===== EDUCATION =====
		List<String> educationLines = new ArrayList<>();
		educationLines.add(
				"Graduation: " +
						dto.getGradDegree() + ", " +
						esc(dto.getGradSpecialization()) + " | " +
						dto.getGradUniversity() + " (" +
						dto.getGradStartYear() + " - " +
						dto.getGradEndYear() + ") | " +
						dto.getGradMarksPercent() + "%");
		educationLines.add(
				"Class XII: " +
						dto.getXiiBoard() + " | " +
						dto.getXiiPassingYear() + " | " +
						dto.getXiiMarksPercent() + "%");
		educationLines.add(
				"Class X: " +
						dto.getxBoard() + " | " +
						dto.getxPassingYear() + " | " +
						dto.getxMarksPercent() + "%");
		sections.add(section("EDUCATION", educationLines));

		// ===== KNOWN LANGUAGES =====
		if (dto.getKnownLanguagesJson() != null && !dto.getKnownLanguagesJson().isEmpty()) {
			sections.add(section(
					"KNOWN LANGUAGES",
					extractKeyWords(dto.getKnownLanguagesJson())));
		}
		


		resume.setSections(sections);

		return resume;
	}

	private ResumeSchemaDTO.Section section(String title, List<String> lines) {
		ResumeSchemaDTO.Section s = new ResumeSchemaDTO.Section();
		s.setTitle(title);
		s.setLines(lines);
		return s;
	}

	// extract skill strings from JSON
	private List<String> extractSkillsFromProjects(String projectsJson) {
		List<String> skills = new ArrayList<>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			List<Map<String, Object>> projectList = mapper.readValue(
					projectsJson,
					new TypeReference<List<Map<String, Object>>>() {
					});
			for (Map<String, Object> p : projectList) {
				Object s = p.get("skills");
				if (s != null)
					skills.add(s.toString());
			}
		} catch (Exception ignore) {
		}
		return skills;
	}
	
	private void appendSocialLink(StringBuilder header, String name, String url) {
	    if (url != null && !url.isBlank()) {
	        header.append(" | <a href='")
	              .append(url)
	              .append("' target='_blank'>")
	              .append(name)
	              .append("</a>");
	    }
	}

	// extract project lines
	private List<String> extractProjectLines(String projectsJson) {
	    List<String> lines = new ArrayList<>();

	    if (projectsJson == null || projectsJson.isBlank()) {
	        logger.warn("Projects JSON is null or empty");
	        return lines;
	    }

	    try {
	        ObjectMapper mapper = new ObjectMapper();

	        List<Map<String, Object>> projectList = mapper.readValue(
	                projectsJson,
	                new TypeReference<List<Map<String, Object>>>() {});

	        for (Map<String, Object> p : projectList) {

	            String title = safeGet(p, "title");
	            String tech = safeGet(p, "technologies");
	            String desc = safeGet(p, "description");
	            String role = safeGet(p, "role");
	            String roleDesc = safeGet(p, "role_description"); // ✅ ADD THIS

	            lines.add(esc(title) + " | " + esc(tech));

	            if (!desc.isBlank()) {
	                lines.add("• " + esc(desc));
	            }

	            if (!role.isBlank()) {
	                lines.add("• Role: " + esc(role));
	            }
	         // Role Description (NEW)
	            if (roleDesc != null && !roleDesc.isBlank()) {
	                lines.add("Role Description: " + roleDesc);
	            }

	            lines.add("");
	        }

	    } catch (Exception e) {
	        logger.error("Failed to parse projects JSON: {}", projectsJson, e);
	    }

	    return lines;
	}
	private String esc(String s) {
		if (s == null)
			return "";
		return s.replace("&", "&amp;")
				.replace("<", "&lt;")
				.replace(">", "&gt;")
				.replace("\"", "&quot;")
				.replace("'", "&#39;");
	}

	private List<String> extractKeyWords(String skillsJson) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(skillsJson, new TypeReference<List<String>>() {
			});
		} catch (Exception e) {
			return List.of();
		}
	}
	private Map<String, String> extractSocialLinksMap(String json) {
	    try {
	        ObjectMapper mapper = new ObjectMapper();
	        return mapper.readValue(json, new TypeReference<Map<String, String>>() {});
	    } catch (Exception e) {
	        return Map.of();
	    }
	}


}
