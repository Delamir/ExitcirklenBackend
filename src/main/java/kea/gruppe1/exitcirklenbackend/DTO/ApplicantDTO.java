package kea.gruppe1.exitcirklenbackend.DTO;

import kea.gruppe1.exitcirklenbackend.models.Applicant;

import java.time.LocalDateTime;


public class ApplicantDTO {

    private Applicant applicant;
    private LocalDateTime time;
    private String reason;

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
