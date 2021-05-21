package com.genesys.resiliency.service;

import com.genesys.resiliency.exception.RetryException;
import com.genesys.resiliency.proxy.QueueServiceProxy;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.QueueObservationQueryResponse;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Service
public class QueueServiceStackedFacade {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceStackedFacade.class);

    @Autowired
    private QueueServiceProxy queueServiceProxy;

    @TimeLimiter(name="genesystimeout")
    @CircuitBreaker(name="genesyscircuit", fallbackMethod = "getQueueInfoFallback")
    @Retry(name="genesysretry")
    @Bulkhead(name="getqueueobservationmetrics")
    public CompletableFuture<QueueObservationQueryResponse> getQueueObservationMetrics(String queueId) throws ApiException, RetryException, IOException {
        CompletableFuture<QueueObservationQueryResponse> future = CompletableFuture
                .supplyAsync(() -> {
                    try {
                        return queueServiceProxy.getQueueObservationMetrics(queueId);
                    }
                    catch (Exception e) {
                        throw new CompletionException(e);
                    }
                });
        return future;
    }

    public CompletableFuture<QueueEntityListing> getQueueInfoFallback(String queueId, ApiException e) {
        QueueEntityListing response = new QueueEntityListing();

        LOGGER.info("I am in the fallback");
        return CompletableFuture.completedFuture(response);
    }

    @TimeLimiter( name ="genesystimeout")
    @CircuitBreaker(name="genesyscircuit")
    @Retry(name="genesysretry")
    @Bulkhead(name="getqueueinfo")
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
