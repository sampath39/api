package com.talentstream.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.TopicProgress;

public interface TopicProgressRepository extends JpaRepository<TopicProgress, Long> {

    List<TopicProgress> findByCourseProgressId(Long courseProgressId);

    Optional<TopicProgress> findByCourseProgressIdAndTopicIndex(Long courseProgressId, int topicIndex);
}