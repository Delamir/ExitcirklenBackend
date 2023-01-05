package kea.gruppe1.exitcirklenbackend.config;

import kea.gruppe1.exitcirklenbackend.models.City;
import kea.gruppe1.exitcirklenbackend.security.services.UserDetailsImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;

import java.util.Arrays;

@TestConfiguration
public class SpringSecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {

        City city1 = new City();
        city1.setName("Andeby");
        City city2 = new City();
        city2.setName("Gåseråd");



        UserDetailsImpl adminUser = new UserDetailsImpl(1L, "admin", "email1@email.email", city1, "123456789", "x", Arrays.asList(
                new SimpleGrantedAuthority(EmployeeResponsibility.ADMINISTRATOR.name())
        ));
        UserDetailsImpl visitUser = new UserDetailsImpl(2L, "visitator", "email2@email.email", city2, "123456789", "y", Arrays.asList(
                new SimpleGrantedAuthority(EmployeeResponsibility.VISITATOR.name())
        ));
        UserDetailsImpl groupUser = new UserDetailsImpl(3L, "gruppeansvarlig", "email3@email.email", city2, "123456789", "z", Arrays.asList(
                new SimpleGrantedAuthority(EmployeeResponsibility.GRUPPEANSVARLIG.name())
        ));


        return new InMemoryUserDetailsManager(Arrays.asList(
                adminUser, visitUser, groupUser
        ));
    }



}
