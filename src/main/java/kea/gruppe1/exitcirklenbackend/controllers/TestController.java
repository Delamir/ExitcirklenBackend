package kea.gruppe1.exitcirklenbackend.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test/all")
    public String allAccess() {


        return "Public Content.";
    }

    @GetMapping("/test/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/test/mod")
    @PreAuthorize("isAuthenticated()")
    public String moderatorAccess() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        System.out.println(authentication.getPrincipal());
        return "Moderator Board.";
    }

    @GetMapping("/test/admin")
    @PreAuthorize("hasAuthority('ADMINSTRATOR')")
    public String adminAccess() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        System.out.println(authentication.getPrincipal());
        System.out.println(authentication.getCredentials());

        return "Admin Board.";
    }
}
