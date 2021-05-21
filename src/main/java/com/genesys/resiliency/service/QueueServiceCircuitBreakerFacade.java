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

/*
 * This class implements the Resilience 4J circuit breaker logic.  Configuration for the @Circuitbreaker annotation can be found in the src/main/resources/application.yml file.
 * You can test this code by running src/python/getMetrics.py script with the circuitbreaker parameter (e.g. getMetrics.py circuitbreaker).  That will hit this
 * specific code.
 */

@Service
public class QueueServiceCircuitBreakerFacade {

    @Autowired
    private QueueServiceProxy queueServiceProxy;

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceCircuitBreakerFacade.class);

    @CircuitBreaker(name = "genesyscircuit")
    public QueueEntityListing getQueueInfo(String queueName) throws IOException, ApiException {
        return queueServiceProxy.getQueueInfo(queueName);
    }

    @CircuitBreaker(name = "genesyscircuit")
    public QueueObservationQueryResponse getQueueObservationMetrics(String queueId)
            throws ApiException, RetryException, IOException {
        return queueServiceProxy.getQueueObservationMetrics(queueId);
    }
}
