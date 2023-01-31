package kea.gruppe1.exitcirklenbackend.repositories;

import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kea.gruppe1.exitcirklenbackend.models.Employee;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import kea.gruppe1.exitcirklenbackend.services.GraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

import java.util.List;

public class EmployeeRepositoryImpl implements EmployeeRepositoryCustom {

    @Autowired
    GraphService graphService;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Employee> findAllWithAad(OAuth2AuthorizedClient graph) {

return null;

    }
}
