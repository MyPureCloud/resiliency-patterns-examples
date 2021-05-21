package com.genesys.resiliency.service;

import com.genesys.resiliency.exception.RetryException;
import com.genesys.resiliency.proxy.QueueServiceProxy;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.QueueObservationQueryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class QueueServiceCircuitBreakerFacade {

    @Autowired
    private QueueServiceProxy queueServiceProxy;

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceCircuitBreakerFacade.class);

    @CircuitBreaker(name="genesyscircuit")
    public QueueEntityListing getQueueInfo(String queueName) throws IOException, ApiException {
        return queueServiceProxy.getQueueInfo(queueName);
    }

    @CircuitBreaker(name="genesyscircuit")
    public QueueObservationQueryResponse getQueueObservationMetrics(String queueId) throws ApiException, RetryException, IOException {
        return queueServiceProxy.getQueueObservationMetrics(queueId);
    }
}
