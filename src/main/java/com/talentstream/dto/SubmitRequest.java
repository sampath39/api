package com.talentstream.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SubmitRequest {
    @NotNull(message = "Question ID is required")
    private Long questionId;

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Code is required")
    private String code;

    public SubmitRequest() {}

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
