package kea.gruppe1.exitcirklenbackend.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Table(name = "applicant_groups")
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ApplicantGroup {

    @Id
    @Column
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column
    private String city;

    @Column
    private String name;

    @Column
    private String address;

    @Column
    private int groupSize;

    @Column
    private int price;

    @Column
    private LocalDateTime startDate;

    @Column
    private boolean discount;

    @Column
    private String tags;

    @Column
    private String description;


    @Column
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    private List<Applicant> inviteList = new ArrayList<>();

    public void addToInviteList(Applicant applicant){
        inviteList.add(applicant);
        applicant.setGroup(this);

    }

    public void removeFromInviteList(Applicant applicant) {
        inviteList.remove(applicant);
        applicant.setGroup(null);
    }



}
