package kea.gruppe1.exitcirklenbackend.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Table(name = "groups")
@Entity
public class Group {

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
}
