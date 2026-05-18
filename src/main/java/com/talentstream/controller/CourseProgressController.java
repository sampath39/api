package com.talentstream.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.talentstream.dto.ProgressRequest;
import com.talentstream.entity.CourseProgress;
import com.talentstream.entity.TopicProgress;
import com.talentstream.repository.CourseProgressRepository;
import com.talentstream.repository.TopicProgressRepository;
import com.talentstream.repository.CourseTopicMasterRepository;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin
public class CourseProgressController {

    @Autowired
    private CourseProgressRepository courseRepo;

    @Autowired
    private TopicProgressRepository topicRepo;

    @Autowired
    private CourseTopicMasterRepository topicMasterRepo;

    // =========================================
    // ENTITY MANAGER
    // =========================================
    @PersistenceContext
    private EntityManager entityManager;

    // =========================================
    // FIX DATABASE COLUMN
    // =========================================
    @GetMapping("/fix-column")
    public String fixColumn() {

        entityManager.createNativeQuery(
            "ALTER TABLE topic_progress " +
            "ADD COLUMN IF NOT EXISTS lesson_location VARCHAR(500)"
        ).executeUpdate();

        return "DONE";
    }

    // =========================================
    // SAVE / UPDATE PROGRESS
    // =========================================
    @PostMapping
    public CourseProgress saveProgress(
            @RequestBody ProgressRequest req
    ) {

        CourseProgress courseProgress = courseRepo
                .findByApplicantIdAndCourseId(
                        req.getApplicantId(),
                        req.getCourseId()
                )
                .orElseGet(() -> {

                    CourseProgress cp =
                            new CourseProgress();

                    cp.setApplicantId(
                            req.getApplicantId()
                    );

                    cp.setCourseId(
                            req.getCourseId()
                    );

                    cp.setCourseName(
                            req.getCourseName()
                    );

                    cp.setOverallProgress(0);

                    cp.setUpdatedAt(
                            LocalDateTime.now()
                    );

                    return courseRepo.save(cp);
                });

        TopicProgress topicProgress = topicRepo
                .findByCourseProgressIdAndTopicIndex(
                        courseProgress.getId(),
                        req.getTopicIndex()
                )
                .orElseGet(() -> {

                    TopicProgress tp =
                            new TopicProgress();

                    tp.setCourseProgressId(
                            courseProgress.getId()
                    );

                    tp.setTopicIndex(
                            req.getTopicIndex()
                    );

                    tp.setTopicName(
                            req.getTopicName()
                    );

                    return tp;
                });

        // =========================================
        // SAVE PROGRESS
        // =========================================
        topicProgress.setTopicProgress(
                req.getTopicProgress()
        );

        // =========================================
        // SAVE RESUME LOCATION
        // =========================================
        topicProgress.setLessonLocation(
                req.getLessonLocation()
        );

        topicRepo.save(topicProgress);

        // =========================================
        // RECALCULATE OVERALL PROGRESS
        // =========================================
        List<TopicProgress> topics =
                topicRepo.findByCourseProgressId(
                        courseProgress.getId()
                );

        long totalTopics =
                topicMasterRepo.countByCourseId(
                        req.getCourseId()
                );

        int totalProgress = 0;

        for (TopicProgress topic : topics) {

            totalProgress +=
                    topic.getTopicProgress();
        }

        int overall =
                totalTopics == 0
                        ? 0
                        : Math.round(
                                (float) totalProgress
                                        / totalTopics
                        );

        courseProgress.setOverallProgress(
                overall
        );

        courseProgress.setUpdatedAt(
                LocalDateTime.now()
        );

        return courseRepo.save(courseProgress);
    }

    // =========================================
    // GET ALL COURSES
    // =========================================
    @GetMapping("/applicant/{applicantId}")
    public List<CourseProgress>
    getCoursesByApplicant(
            @PathVariable Long applicantId
    ) {

        return courseRepo.findByApplicantId(
                applicantId
        );
    }

    // =========================================
    // GET TOPICS
    // =========================================
    @GetMapping("/topics/{courseProgressId}")
    public List<TopicProgress> getTopics(
            @PathVariable Long courseProgressId
    ) {

        return topicRepo.findByCourseProgressId(
                courseProgressId
        );
    }
}