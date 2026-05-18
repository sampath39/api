package com.talentstream.dto;

public class RunTestCaseResult {
    private Long id;
    private String input;
    private String expectedOutput;
    private String actualOutput;
    private boolean passed;
    private boolean isPrivate;
    private String error;

    public RunTestCaseResult() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getInput() { return input; }
    public void setInput(String input) { this.input = input; }

    public String getExpectedOutput() { return expectedOutput; }
    public void setExpectedOutput(String expectedOutput) { this.expectedOutput = expectedOutput; }

    public String getActualOutput() { return actualOutput; }
    public void setActualOutput(String actualOutput) { this.actualOutput = actualOutput; }

    public boolean isPassed() { return passed; }
    public void setPassed(boolean passed) { this.passed = passed; }

    public boolean isPrivate() { return isPrivate; }
    public void setPrivate(boolean aPrivate) { isPrivate = aPrivate; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
