package kea.gruppe1.exitcirklenbackend.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "employees")
@Entity(name = "Employee")
public class Employee {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private String email;

    @Column
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private EmployeeResponsibility role;

    @Column
    private String password;

    public Employee() {

    }

    public Employee(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
