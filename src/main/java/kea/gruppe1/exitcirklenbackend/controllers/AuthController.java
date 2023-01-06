package kea.gruppe1.exitcirklenbackend.controllers;

import io.jsonwebtoken.impl.DefaultClaims;
import kea.gruppe1.exitcirklenbackend.DTO.JwtResponse;
import kea.gruppe1.exitcirklenbackend.DTO.LoginRequest;
import kea.gruppe1.exitcirklenbackend.DTO.TokenRefreshRequest;
import kea.gruppe1.exitcirklenbackend.DTO.TokenRefreshResponse;
import kea.gruppe1.exitcirklenbackend.exceptions.TokenRefreshException;
import kea.gruppe1.exitcirklenbackend.models.Employee;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import kea.gruppe1.exitcirklenbackend.models.RefreshToken;
import kea.gruppe1.exitcirklenbackend.repositories.EmployeeRepository;
import kea.gruppe1.exitcirklenbackend.security.jwt.JwtUtils;
import kea.gruppe1.exitcirklenbackend.security.services.UserDetailsImpl;
import kea.gruppe1.exitcirklenbackend.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * made with help from https://www.bezkoder.com/spring-boot-react-jwt-auth/
 */
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

    @Autowired
    RefreshTokenService refreshTokenService;

    /**
     * Handles the log in request of a use
     * @param loginRequest the login details that needs to be authenticated
     * @return a response entity with user details of the authenticated user
     */
    @PostMapping("/auth/signin")
    public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        System.out.println(authentication);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
                userDetails.getId(),
                userDetails.getCity(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getPhone(),
                roles));
    }


    /**
     * Handles refresh token so the session is still valid after browser is closed
     * @param request the requested refresh token
     * @return a refresh token
     */
    @PostMapping("/auth/refreshtoken")
    public ResponseEntity<?> refreshtoken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getEmployee)
                .map(employee -> {
                    String token = jwtUtils.generateTokenFromEmail(employee.getEmail());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        return new HashMap<String, Object>(claims);
    }

    /**
     * Handles sign out of a logged in user
     * @return a response entity ok with a specified message
     */
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok("Log out successful!");
    }

    /**
     * Seeds an admin user if the database is wiped
     * @return a response entity ok with a specified message
     */
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

        employee.setRole(EmployeeResponsibility.ADMINISTRATOR);
        employeeRepository.save(employee);

        return ResponseEntity.ok("User registered successfully!");
    }


}