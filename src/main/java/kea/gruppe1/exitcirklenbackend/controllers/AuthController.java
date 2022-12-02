package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.DTO.JwtResponse;
import kea.gruppe1.exitcirklenbackend.DTO.LoginRequest;
import kea.gruppe1.exitcirklenbackend.DTO.SignupRequest;
import kea.gruppe1.exitcirklenbackend.models.Employee;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import kea.gruppe1.exitcirklenbackend.repositories.EmployeeRepository;
import kea.gruppe1.exitcirklenbackend.security.jwt.JwtUtils;
import kea.gruppe1.exitcirklenbackend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPhone(),
                roles));
    }


    @PostMapping("/auth/adminsignup")
    public ResponseEntity<?> registerUser() {
        if (employeeRepository.existsByEmail("admin@admin.admin")) {
            return ResponseEntity
                    .badRequest()
                    .body("Error: Email is already in use!");
        }

        // Create new user's account
        Employee employee = new Employee("admin@admin.admin",
                encoder.encode("admin"));

        EmployeeResponsibility responsibility = EmployeeResponsibility.ADMINSTRATOR;



        employee.setResponsibility(responsibility);
        employeeRepository.save(employee);

        return ResponseEntity.ok("User registered successfully!");
    }



}