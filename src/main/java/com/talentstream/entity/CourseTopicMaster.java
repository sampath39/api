package com.talentstream.entity;

import javax.persistence.*;

@Entity
@Table(
    name = "course_topic_master",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_id", "topic_index"})
    }
)
public class CourseTopicMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Long courseId;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "topic_index", nullable = false)
    private int topicIndex;

    @Column(name = "topic_name", nullable = false)
    private String topicName;

    public CourseTopicMaster() {}

    public Long getId() {
        return id;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTopicIndex() {
        return topicIndex;
    }

    public void setTopicIndex(int topicIndex) {
        this.topicIndex = topicIndex;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}