package com.talentstream.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.CourseProgress;

public interface CourseProgressRepository extends JpaRepository<CourseProgress, Long> {

    Optional<CourseProgress> findByApplicantIdAndCourseId(Long applicantId, Long courseId);

    List<CourseProgress> findByApplicantId(Long applicantId);
}