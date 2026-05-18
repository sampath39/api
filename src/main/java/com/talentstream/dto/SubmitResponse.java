package com.talentstream.dto;

import java.util.List;

public class SubmitResponse {
    private String status; // PASSED, FAILED, ERROR
    private int testCasesPassed;
    private int totalTestCases;
    private String error;
    private List<RunTestCaseResult> failedTests;

    public SubmitResponse() {}

    public SubmitResponse(String status, int testCasesPassed, int totalTestCases) {
        this.status = status;
        this.testCasesPassed = testCasesPassed;
        this.totalTestCases = totalTestCases;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getTestCasesPassed() { return testCasesPassed; }
    public void setTestCasesPassed(int testCasesPassed) { this.testCasesPassed = testCasesPassed; }

    public int getTotalTestCases() { return totalTestCases; }
    public void setTotalTestCases(int totalTestCases) { this.totalTestCases = totalTestCases; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public List<RunTestCaseResult> getFailedTests() { return failedTests; }
    public void setFailedTests(List<RunTestCaseResult> failedTests) { this.failedTests = failedTests; }
}
