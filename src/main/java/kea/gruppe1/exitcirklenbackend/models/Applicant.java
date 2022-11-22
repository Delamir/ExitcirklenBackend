package kea.gruppe1.exitcirklenbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Table(name = "applicants")
@Entity(name = "Applicant")
public class Applicant {

    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String age;

    @Column
    private String gender;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Column
    private String city;

    @Enumerated(EnumType.ORDINAL)
    private ApplicantStatus status;
    
    @Column
    private String description;

    @Column
    private int priority;

    @Column
    private boolean contactCall;

    @Column
    private boolean contactText;

    @Column
    private int userType;

    @Column
    private boolean paidStatus;

    @Column
    private boolean answeredSurvey = false;

    @Column
    private LocalDateTime lastChanged = LocalDateTime.now();

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicantGroup group;
}
