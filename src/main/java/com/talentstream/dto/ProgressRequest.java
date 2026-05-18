package com.talentstream.dto;

public class ProgressRequest {

    private Long applicantId;

    private Long courseId;

    private String courseName;

    private int topicIndex;

    private String topicName;

    private int topicProgress;

    // =========================================
    // SCORM RESUME LOCATION
    // =========================================
    private String lessonLocation;

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(
        Long applicantId
    ) {
        this.applicantId =
            applicantId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(
        Long courseId
    ) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(
        String courseName
    ) {
        this.courseName =
            courseName;
    }

    public int getTopicIndex() {
        return topicIndex;
    }

    public void setTopicIndex(
        int topicIndex
    ) {
        this.topicIndex =
            topicIndex;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(
        String topicName
    ) {
        this.topicName =
            topicName;
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