package kea.gruppe1.exitcirklenbackend.models;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Table(name = "cities")
@Entity(name = "City")
public class City {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String address;

    @OneToMany
    private List<Employee> employeeList;

    @OneToMany
    private List<Applicant> applicantList;

    @OneToMany
    private List<ApplicantGroup> applicantGroupList;

}
