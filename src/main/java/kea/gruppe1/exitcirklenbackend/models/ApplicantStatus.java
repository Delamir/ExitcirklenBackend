package kea.gruppe1.exitcirklenbackend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Table(name = "status")
@Entity
public class ApplicantStatus {

    @Id
    @Column
    private Long id;

    @Column
    private String status;

    @OneToOne
    private Applicant applicant;

    @Column
    private LocalDateTime assignedTime = LocalDateTime.now();

    public String getAssignedTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return assignedTime.format(formatter);
    }

}
