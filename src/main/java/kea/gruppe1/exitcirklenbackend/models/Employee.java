package kea.gruppe1.exitcirklenbackend.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Table(name = "employees")
@Entity(name = "Employee")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @ManyToOne
    private City city;

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
