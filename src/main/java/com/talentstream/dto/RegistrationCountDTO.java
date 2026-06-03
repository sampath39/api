package com.talentstream.dto;

public class RegistrationCountDTO {
    private long totalRegistrations;
    private String hackathonName;

    public RegistrationCountDTO(long totalRegistrations) {
        this.totalRegistrations = totalRegistrations;
        this.hackathonName = "All Hackathons";
    }

    public RegistrationCountDTO(long totalRegistrations, String hackathonName) {
        this.totalRegistrations = totalRegistrations;
        this.hackathonName = hackathonName;
    }

    public long getTotalRegistrations() {
        return totalRegistrations;
    }

    public void setTotalRegistrations(long totalRegistrations) {
        this.totalRegistrations = totalRegistrations;
    }

    public String getHackathonName() {
        return hackathonName;
    }

    public void setHackathonName(String hackathonName) {
        this.hackathonName = hackathonName;
    }
}
 