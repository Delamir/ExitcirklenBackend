package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Employee;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import kea.gruppe1.exitcirklenbackend.repositories.EmployeeRepository;
import kea.gruppe1.exitcirklenbackend.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    PasswordEncoder encoder;

    /**
     * Gets all employees
     * @return a list of employees
     */
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Gets one employee
     * @param id the id on the employee
     * @return the employee or null if the employee is not found
     */
    @GetMapping("/employees/{id}")
    public Employee getEmployee(@PathVariable Long id) {
        return employeeRepository.findById(id).orElse(null);
    }

    /**
     * Gets all employee responsibilities
     * @return a list of employee responsibilities
     */
    @GetMapping("/employees/responsibility")
    public List<EmployeeResponsibility> getResponsibilities() {
        return new ArrayList<>(Arrays.asList(EmployeeResponsibility.values()));
    }

    /**
     * Creates a new employee in the database
     * @param newEmployee the data on the employee
     * @return the saved employee
     */
    @PostMapping("/employees")
    public Employee addEmployee(@RequestBody Employee newEmployee) {
        newEmployee.setPassword(encoder.encode(newEmployee.getPassword()));
        return employeeRepository.save(newEmployee);
    }

    /**
     * Replace and employee in the database with new data
     * @param id the id of them employee
     * @param employeeToUpdateWith the object with the new data
     * @return either a http status of 200 or a status of 400 if something goes wrong
     */
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

    /**
     * Updates and employee in the database
     * @param id the id of the employee
     * @param employeeToUpdate the object with the new data
     * @return either a http status of 200 or a status of 400 if something goes wrong
     */
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

    /**
     * Deletes a specific employee
     * @param id the employee id
     * @return either a http status of 200 or a status of 400 if something goes wrong
     */
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
