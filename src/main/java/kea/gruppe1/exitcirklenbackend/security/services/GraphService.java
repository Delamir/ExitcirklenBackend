package kea.gruppe1.exitcirklenbackend.security.services;

import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.authentication.TokenCredentialAuthProvider;
import com.microsoft.graph.models.User;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import okhttp3.Request;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GraphService {
    @Value("${exitcirklen.app.clientId}")
    String clientId;
    @Value("${exitcirklen.app.tenantId}")
    String tenant;
    @Value("${exitcirklen.app.clientSecret}")
    String clientSecret;

    List<String> scopes = new ArrayList<>(Arrays.asList("https://graph.microsoft.com/.default"));

    ClientSecretCredential provider;
    TokenCredentialAuthProvider tokenCredentialAuthProvider;
    GraphServiceClient<Request> graphClient;

    //User me = graphClient.me().buildRequest().get();

    public void testTing() {
        provider = new ClientSecretCredentialBuilder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .tenantId(tenant)
                .build();
        tokenCredentialAuthProvider = new TokenCredentialAuthProvider(scopes, provider);

        graphClient = GraphServiceClient
                .builder()
                .authenticationProvider(tokenCredentialAuthProvider)
                .buildClient();
        //User me = graphClient.me().buildRequest().get();
        User gottenUser = graphClient.users("admin@exitcirklendk.onmicrosoft.com").buildRequest().get();

        UserCollectionPage users = graphClient.users().buildRequest().get();

        if (users != null) {
            List<User> currentUsers = users.getCurrentPage();
            for(User user : currentUsers){
                System.out.println(user.displayName);
            }
        }
    }
}
