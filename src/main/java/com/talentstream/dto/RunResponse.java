package com.talentstream.dto;

public class RunResponse {
    private String output;
    private String error;

    public RunResponse() {}

    public RunResponse(String output, String error) {
        this.output = output;
        this.error = error;
    }

    public String getOutput() { return output; }
    public void setOutput(String output) { this.output = output; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}
