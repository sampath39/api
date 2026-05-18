package com.talentstream.repository;
import com.talentstream.entity.SocialLinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface SocialLinksRepository extends JpaRepository<SocialLinks, Long>{
	
	// Fetch social links by applicantId
	Optional<SocialLinks> findByApplicantId(Long applicantId);

	// Check if social links already exist for an applicant
	boolean existsByApplicantId(Long applicantId);

	// Delete by applicantId
	void deleteByApplicantId(Long applicantId);

}
