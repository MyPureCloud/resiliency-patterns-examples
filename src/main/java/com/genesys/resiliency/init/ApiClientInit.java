package com.genesys.resiliency.init;

import com.genesys.resiliency.config.GenesysClientConfig;
import com.mypurecloud.sdk.v2.ApiClient;
import com.mypurecloud.sdk.v2.ApiResponse;
import com.mypurecloud.sdk.v2.PureCloudRegionHosts;
import com.mypurecloud.sdk.v2.extensions.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.EventListener;

@Configuration
@DependsOn("GenesysClientConfig")
public class ApiClientInit {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClientInit.class);

    @Autowired
    private GenesysClientConfig clientConfig;

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        PureCloudRegionHosts region = PureCloudRegionHosts.us_east_1;

        ApiClient.RetryConfiguration retryConfiguration = new ApiClient.RetryConfiguration();
       // retryConfiguration.setMaxRetryTimeSec(60);   //Sets the maximum retry window to 60 be seconds

        ApiClient apiClient = ApiClient.Builder
                .standard()
                .withBasePath(region)
                .withRetryConfiguration(retryConfiguration)
                .build();

        ApiResponse<AuthResponse> authResponse = null;
        try {
            authResponse = apiClient.authorizeClientCredentials(clientConfig.getClientId(), clientConfig.getClientSecret());
        } catch (Exception e) {
            LOGGER.error(String.valueOf(e));
        }

        // Use the ApiClient instance
        com.mypurecloud.sdk.v2.Configuration.setDefaultApiClient(apiClient);
    }
}
