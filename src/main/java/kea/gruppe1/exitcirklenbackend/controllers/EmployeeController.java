package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Employee;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import kea.gruppe1.exitcirklenbackend.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class EmployeeController {

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    @GetMapping("/employee/responsibility")
    public List<EmployeeResponsibility> getResponsibilities() {
        return new ArrayList<>(Arrays.asList(EmployeeResponsibility.values()));
    }

    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee newEmployee) {
        newEmployee.setPassword(encoder.encode(newEmployee.getPassword()));
        return employeeRepository.save(newEmployee);
    }

    @PutMapping("/employees/{id}")
    public HttpStatus updateEmployeeById(@PathVariable Long id, @RequestBody Employee employeeToUpdateWith) {
        if (employeeRepository.existsById(id)) {
            if(!employeeToUpdateWith.getId().equals(id)) {
                employeeRepository.deleteById(id);
            }
            employeeToUpdateWith.setPassword(encoder.encode(employeeToUpdateWith.getPassword()));
            employeeRepository.save(employeeToUpdateWith);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @DeleteMapping("/employees/{id}")
    public HttpStatus deleteEmployeeById(@PathVariable Long id) {
        try {
            employeeRepository.deleteById(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }
}
