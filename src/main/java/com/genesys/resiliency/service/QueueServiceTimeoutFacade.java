package com.genesys.resiliency.service;

import com.genesys.resiliency.exception.RetryException;
import com.genesys.resiliency.proxy.QueueServiceProxy;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.QueueObservationQueryResponse;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class QueueServiceTimeoutFacade {
    @Autowired
    private QueueServiceProxy queueServiceProxy;

    @TimeLimiter(name = "genesystimeout")
    public CompletableFuture<QueueObservationQueryResponse> getQueueObservationMetrics(String queueId) throws ApiException, RetryException, IOException {
        CompletableFuture<QueueObservationQueryResponse> future = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return queueServiceProxy.getQueueObservationMetrics(queueId);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                });
        return future;
    }

    @TimeLimiter(name = "genesystimeout")
    public CompletableFuture<QueueEntityListing> getQueueInfo(String queueName) {
        CompletableFuture<QueueEntityListing> future = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return queueServiceProxy.getQueueInfo(queueName);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                });
        return future;
    }
}
