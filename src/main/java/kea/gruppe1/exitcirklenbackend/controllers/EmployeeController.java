package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Employee;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import kea.gruppe1.exitcirklenbackend.repositories.EmployeeRepository;
import kea.gruppe1.exitcirklenbackend.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMINISTRATOR')")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RefreshTokenService refreshTokenService;


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
        //newEmployee.setPassword(encoder.encode(newEmployee.getPassword()));
        return employeeRepository.save(newEmployee);
    }

    @PutMapping("/employees/{id}")
    public HttpStatus updateEmployeeById(@PathVariable Long id, @RequestBody Employee employeeToUpdateWith) {
        if (employeeRepository.existsById(id)) {
            if(!employeeToUpdateWith.getId().equals(id)) {
                employeeRepository.deleteById(id);
            }
          //  employeeToUpdateWith.setPassword(encoder.encode(employeeToUpdateWith.getPassword()));
            employeeRepository.save(employeeToUpdateWith);
            return HttpStatus.OK;
        } else {
            return HttpStatus.BAD_REQUEST;
        }
    }

    @PatchMapping("/employees/{id}")
    public HttpStatus updateEmployees(@PathVariable Long id,
                                      @RequestBody Employee employeeToUpdate) {
        employeeRepository.findById(id).map(employee -> {
            if (employeeToUpdate.getName() != null) employee.setName(employeeToUpdate.getName());
            if (employeeToUpdate.getAge() != 0) employee.setAge(employeeToUpdate.getAge());
            if (employeeToUpdate.getEmail() != null) employee.setEmail(employeeToUpdate.getEmail());
            if (employeeToUpdate.getCity() != null) employee.setCity(employeeToUpdate.getCity());
            if (employeeToUpdate.getPhoneNumber() != null) employee.setPhoneNumber(employeeToUpdate.getPhoneNumber());
            if (employeeToUpdate.getRole() != null) employee.setRole(employeeToUpdate.getRole());

            employeeRepository.save(employee);
            return HttpStatus.OK;
        });
        return HttpStatus.BAD_REQUEST;
    }

    @DeleteMapping("/employees/{id}")
    public HttpStatus deleteEmployeeById(@PathVariable Long id) {
        try {
            refreshTokenService.deleteByUserId(id);
            employeeRepository.deleteById(id);
            return HttpStatus.OK;
        } catch (Exception e) {
            e.printStackTrace();
            return HttpStatus.BAD_REQUEST;
        }
    }
}
