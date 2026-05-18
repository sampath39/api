package com.talentstream.entity;

import javax.persistence.*;

@Entity
@Table(
    name = "topic_progress",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {
            "course_progress_id",
            "topic_index"
        })
    }
)
public class TopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
        name = "course_progress_id",
        nullable = false
    )
    private Long courseProgressId;

    @Column(
        name = "topic_index",
        nullable = false
    )
    private int topicIndex;

    @Column(name = "topic_name")
    private String topicName;

    @Column(name = "topic_progress")
    private int topicProgress;

    // =========================================
    // SCORM RESUME LOCATION
    // =========================================
    @Column(name = "lesson_location")
    private String lessonLocation;

    public TopicProgress() {
    }

    public Long getId() {
        return id;
    }

    public Long getCourseProgressId() {
        return courseProgressId;
    }

    public void setCourseProgressId(
        Long courseProgressId
    ) {
        this.courseProgressId =
            courseProgressId;
    }

    public int getTopicIndex() {
        return topicIndex;
    }

    public void setTopicIndex(
        int topicIndex
    ) {
        this.topicIndex = topicIndex;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(
        String topicName
    ) {
        this.topicName = topicName;
    }

    public int getTopicProgress() {
        return topicProgress;
    }

    public void setTopicProgress(
        int topicProgress
    ) {
        this.topicProgress =
            topicProgress;
    }

    // =========================================
    // LESSON LOCATION
    // =========================================
    public String getLessonLocation() {
        return lessonLocation;
    }

    public void setLessonLocation(
        String lessonLocation
    ) {
        this.lessonLocation =
            lessonLocation;
    }
}