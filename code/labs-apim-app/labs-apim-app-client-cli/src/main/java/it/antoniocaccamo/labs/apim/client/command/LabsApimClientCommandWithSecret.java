package it.antoniocaccamo.labs.apim.client.command;

import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import picocli.CommandLine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component @Slf4j
@CommandLine.Command(name = "secret", mixinStandardHelpOptions = true)
public class LabsApimClientCommandWithSecret implements Callable<Integer> {

    @Value("${client.authority}")
    String authority;

    @Value("${client.application-id}")
    private String clientId;

    @Value("${client.with-secret.secret}")
    private String secret;

    @Value("${client.scope}")
    private String scope;

    @Value("${client.endpoint}")
    private String endpoint;

    @Override
    public Integer call() throws Exception {

        int exitCode = 0;

        log.info("authority {} client {} scope {} endpoint {}", authority, clientId, scope, endpoint);

        try {
            ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                            clientId,
                            ClientCredentialFactory.createFromSecret(secret)
                    )
                    .authority(authority)
                    .build();
            ClientCredentialParameters clientCredentialParam = ClientCredentialParameters.builder(
                            Collections.singleton(scope))
                    .build();
            CompletableFuture<IAuthenticationResult> future = app.acquireToken(clientCredentialParam);
            IAuthenticationResult authResult = future.get();
            log.info("access token: OK");

            log.info("calling {}", endpoint);
            URL url = new URL(endpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + authResult.accessToken());
            conn.setRequestProperty("Accept","application/json");

            int httpResponseCode = conn.getResponseCode();
            if(httpResponseCode == HTTPResponse.SC_OK) {
                StringBuilder response;
                try(BufferedReader in = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()))){

                    String inputLine;
                    response = new StringBuilder();
                    while (( inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                }
                log.info("response : {}", response);
                exitCode= 0;
            } else {
                log.error("Connection returned HTTP code: {} with message: {}",
                        httpResponseCode, conn.getResponseMessage());
                exitCode= 1;
            }
        } catch (MalformedURLException | ExecutionException e) {
            log.error("error occurred", e);
            exitCode= -1;
        } finally {
            return exitCode;
        }
    }


}