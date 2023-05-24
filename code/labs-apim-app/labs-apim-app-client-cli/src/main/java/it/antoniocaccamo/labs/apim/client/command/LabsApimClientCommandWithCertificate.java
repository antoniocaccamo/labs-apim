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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component @Slf4j
@CommandLine.Command(name="certificate", mixinStandardHelpOptions = true)
public class LabsApimClientCommandWithCertificate implements Callable<Integer> {

    @Value("${client.authority}")
    String authority;

    @Value("${client.application-id}")
    private String clientId;

    @Value("${client.with-certificate.path.private-key}")
    private String privateKeyPath;

    @Value("${client.with-certificate.path.certificate}")
    private String certificatePath;

    @Value("${client.scope}")
    private String scope;
    
    @Value("${client.endpoint}")
    private String endpoint;

    @Override
    public Integer call() throws Exception {

        int exitCode = 0;

        log.info("authority {} client {} scope {} endpoint {}", authority, clientId, scope, endpoint);
        log.info("key {} cert {}", privateKeyPath, certificatePath);

        try {

            String key = new String(Files.readAllBytes(Paths.get(privateKeyPath))   , StandardCharsets.UTF_8.toString());
            String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll(System.lineSeparator(), "")
                    .replace("-----END PRIVATE KEY-----", "");

            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(encoded);
            PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(spec);

            InputStream certStream = new ByteArrayInputStream(Files.readAllBytes(Paths.get(certificatePath)));
            X509Certificate cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(certStream);

            ConfidentialClientApplication app = ConfidentialClientApplication.builder(
                            clientId,
                            ClientCredentialFactory.createFromCertificate(privateKey, cert)
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
        } catch (Exception e) {
            log.error("error occurred", e);
            exitCode= -1;
        } finally {
            return exitCode;
        }
    }
}
