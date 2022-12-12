package kea.gruppe1.exitcirklenbackend.security.services;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GraphService {


    String clientId = "askufdha";

    String tenant = "Slik mig";
    String clientSecret = "Slik mig2";


    List<String> scopes = new ArrayList<>(Arrays.asList("User.Read"));

    ClientSecretCredential provider = new ClientSecretCredentialBuilder()
            .clientId(clientId)
            .clientSecret(clientSecret)
            .tenantId(tenant)
            .build();
    TokenCredentialAuthProvider tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, provider);


    GraphServiceClient<Request> graphClient = GraphServiceClient
            .builder()
            .authenticationProvider(tokenCredentialAuthProvider)
            .buildClient();

    User me = graphClient.me().buildRequest().get();

}
