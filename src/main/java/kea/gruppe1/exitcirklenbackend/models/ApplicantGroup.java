package kea.gruppe1.exitcirklenbackend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Table(name = "applicant_groups")
@Entity
public class ApplicantGroup {

    @Id
    @Column
    private Long id;

    @Column
    private String city;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private String groupSize;

    @Column
    private String availableSpots;

    @Column
    private String price;

    @Column
    private LocalDateTime startDate;

    @Column
    private boolean discount;

    @Column
    private String tags;


}
