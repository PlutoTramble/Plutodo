package org.plutotramble.client.Nextcloud;

import org.plutotramble.client.Nextcloud.response.GenericOCSResponse;
import org.plutotramble.dto.NextCloudLoginDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class NextcloudClient {

    public NextcloudClient() {}

    @Async
    public Future<Boolean> pingUserAccount(NextCloudLoginDTO nextCloudLogin) {
        WebClient client = createWebClientWithBasicAuthorization(nextCloudLogin);

        try {
            var response = client.get()
                    .uri("/ocs/v2.php/apps/user_status/api/v1/user_status")
                    .retrieve()
                    .bodyToMono(GenericOCSResponse.class)
                    .block();

            return CompletableFuture.completedFuture(true);
        }
        catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    private WebClient createWebClientWithBasicAuthorization(NextCloudLoginDTO nextCloudLogin) {
        return WebClient.builder()
                .baseUrl(nextCloudLogin.serverUrl)
                .defaultHeader("OCS-APIRequest", "true")
                .filter(ExchangeFilterFunctions.basicAuthentication(nextCloudLogin.username, nextCloudLogin.password))
                .build();
    }
}
