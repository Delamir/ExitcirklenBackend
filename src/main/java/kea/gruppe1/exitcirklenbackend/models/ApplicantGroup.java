package kea.gruppe1.exitcirklenbackend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    public String getStartDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return startDate.format(formatter);
    }
}
