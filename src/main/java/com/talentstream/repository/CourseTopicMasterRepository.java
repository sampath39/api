package com.talentstream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.CourseTopicMaster;

public interface CourseTopicMasterRepository extends JpaRepository<CourseTopicMaster, Long> {

    long countByCourseId(Long courseId);

    List<CourseTopicMaster> findByCourseIdOrderByTopicIndexAsc(Long courseId);
}