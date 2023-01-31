package kea.gruppe1.exitcirklenbackend.repositories;

import kea.gruppe1.exitcirklenbackend.models.Employee;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import java.util.List;

public interface EmployeeRepositoryCustom {

    List<Employee> findAllWithAad(OAuth2AuthorizedClient graph);
}
