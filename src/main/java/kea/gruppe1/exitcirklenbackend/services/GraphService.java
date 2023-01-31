package kea.gruppe1.exitcirklenbackend.services;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.BaseAuthenticationProvider;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.*;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kea.gruppe1.exitcirklenbackend.models.Employee;
import kea.gruppe1.exitcirklenbackend.models.EmployeeResponsibility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class GraphService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphService.class);
    private static final String GRAPH_ME_ENDPOINT = "https://graph.microsoft.com/v1.0/me";
    private static final String GRAPH_SEND_MAIL_ENDPOINT = "https://graph.microsoft.com/Mail.Send";

    @Value("${exitcirklen.app.objectId}")
    private String objectId;

    private final WebClient webClient;
    private final OAuth2AuthorizedClientManager auth2AuthorizedClientManager;

    @Value("${spring.cloud.azure.active-directory.credential.client-id}")
    String clientId;

    @Value("${spring.cloud.azure.active-directory.profile.tenant-id}")
    String tenantId;

    @Value("${spring.cloud.azure.active-directory.credential.client-secret}")
    String clientSecret;

    private ClientSecretCredential clientSecretCredential;


    public GraphService(WebClient webClient, OAuth2AuthorizedClientManager auth2AuthorizedClientManager) {
        this.webClient = webClient;
        this.auth2AuthorizedClientManager = auth2AuthorizedClientManager;
    }


    public Employee getCurrentEmployee(OAuth2AuthorizedClient graph) {
        GraphServiceClient graphServiceClient = getGraphServiceClient(graph);


        Employee employeeToReturn = new Employee();
        User user = graphServiceClient.me().buildRequest().get();

        employeeToReturn.setName(user.displayName);
        employeeToReturn.setEmail(user.userPrincipalName);

        AppRoleAssignmentCollectionPage rolesAssignments = graphServiceClient.me().appRoleAssignments().buildRequest().filter("resourceId eq " + objectId).get();
        UUID roleId = rolesAssignments.getCurrentPage().get(0).appRoleId;


        ServicePrincipal sss = graphServiceClient.servicePrincipals(objectId).buildRequest().get();


        for (AppRole role : sss.appRoles) {
            if (role.id.equals(roleId))
                employeeToReturn.setRole(EmployeeResponsibility.valueOf(role.displayName));
        }
        return employeeToReturn;
    }

    public AppRole getRoleOfUser(User user, GraphServiceClient graphServiceClient) {
        AppRoleAssignmentCollectionPage rolesAssignments = graphServiceClient.users(user.id).appRoleAssignments().buildRequest().filter("resourceId eq " + objectId).get();
        UUID roleId = rolesAssignments.getCurrentPage().get(0).appRoleId;


        ServicePrincipal sss = graphServiceClient.servicePrincipals(objectId).buildRequest().get();


        for (AppRole role : sss.appRoles) {
            if (role.id.equals(roleId))
                return role;
        }
        return null;
    }

    public GraphServiceClient getGraphServiceClient(OAuth2AuthorizedClient auth) {

        return GraphServiceClient
                .builder()
                .authenticationProvider(new GraphAuthenticationProvider(auth))
                .buildClient();
    }

    public GraphServiceClient getAppGraphServiceClient() {

        if (clientSecretCredential == null) {
            clientSecretCredential = new ClientSecretCredentialBuilder()
                    .clientId(clientId)
                    .tenantId(tenantId)
                    .clientSecret(clientSecret)
                    .build();
        }

        TokenCredentialAuthProvider authProvider =
                new TokenCredentialAuthProvider(List.of("https://graph.microsoft.com/.default"), clientSecretCredential);
       // new TokenCredentialAuthProvider(List.of("Mail.send", "User.Read.All"), clientSecretCredential);


        return GraphServiceClient
                .builder()
                .authenticationProvider(authProvider)
                .buildClient();

    }

    public void fillEmployeeAadFields(List<Employee> employees, OAuth2AuthorizedClient graph) {

        GraphServiceClient graphServiceClient = getGraphServiceClient(graph);

        for (Employee employee : employees) {

            User user = graphServiceClient.users(employee.getEmail()).buildRequest().get();

            employee.setName(user.displayName);
            employee.setPhoneNumber(user.mobilePhone);
            employee.setRole(EmployeeResponsibility.valueOf(getRoleOfUser(user, graphServiceClient).displayName));
        }
    }


    private static class GraphAuthenticationProvider
            extends BaseAuthenticationProvider {

        private OAuth2AuthorizedClient graphAuthorizedClient;

        /**
         * Set up the GraphAuthenticationProvider. Allows accessToken to be
         * used by GraphServiceClient through the interface IAuthenticationProvider
         *
         * @param graphAuthorizedClient OAuth2AuthorizedClient created by AAD Boot starter. Used to surface the access token.
         */
        public GraphAuthenticationProvider(@Nonnull OAuth2AuthorizedClient graphAuthorizedClient) {
            this.graphAuthorizedClient = graphAuthorizedClient;
        }

        /**
         * This implementation of the IAuthenticationProvider helps injects the Graph access
         * token into the headers of the request that GraphServiceClient makes.
         *
         * @param requestUrl the outgoing request URL
         * @return a future with the token
         */
        @Override
        public CompletableFuture<String> getAuthorizationTokenAsync(@Nonnull final URL requestUrl) {
            return CompletableFuture.completedFuture(graphAuthorizedClient.getAccessToken().getTokenValue());
        }
    }

}
