package kea.gruppe1.exitcirklenbackend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "employees")
@Entity(name = "Employee")
public class Employee {

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Enumerated(EnumType.ORDINAL)
    private EmployeeResponsibility responsibility;

    @Column
    private String password;


}
