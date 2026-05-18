package com.talentstream.dto;

import java.util.List;

public class RunMultipleResponse {
    private List<RunTestCaseResult> results;
    private String error;

    public RunMultipleResponse() {}

    public RunMultipleResponse(List<RunTestCaseResult> results) {
        this.results = results;
    }

    public List<RunTestCaseResult> getResults() { return results; }
    public void setResults(List<RunTestCaseResult> results) { this.results = results; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
