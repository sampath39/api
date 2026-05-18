package com.talentstream.repository;

import com.talentstream.entity.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    java.util.List<Submission> findByQuestionIdOrderBySubmittedAtDesc(Long questionId);
}
