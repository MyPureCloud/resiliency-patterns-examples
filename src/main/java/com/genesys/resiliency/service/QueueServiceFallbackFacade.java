package com.genesys.resiliency.service;

import com.genesys.resiliency.exception.RetryException;
import io.github.resilience4j.retry.annotation.Retry;
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
import java.util.concurrent.ConcurrentHashMap;

@Service
public class QueueServiceFallbackFacade {

    private ConcurrentHashMap<String, QueueObservationQueryResponse> fallbackMetrics = new ConcurrentHashMap<String, QueueObservationQueryResponse>();

    @Autowired
    private QueueServiceProxy queueServiceProxy;

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceFallbackFacade.class);

    @CircuitBreaker(name="genesysfallback")
    //@Retry(name="genesysretry")
    public QueueEntityListing getQueueInfo(String queueName) throws IOException, ApiException {
        return queueServiceProxy.getQueueInfo(queueName);
    }

    @CircuitBreaker(name="genesysfallback", fallbackMethod = "getQueueObservationMetricFallback")
   // @Retry(name="genesysretry")
    public QueueObservationQueryResponse getQueueObservationMetrics(String queueId) throws ApiException, RetryException, IOException {
        QueueObservationQueryResponse response =  queueServiceProxy.getQueueObservationMetrics(queueId);
        fallbackMetrics.put(queueId,response);
        return response;
    }

    public  QueueObservationQueryResponse getQueueObservationMetricFallback(String queueId,Exception e) {
        LOGGER.info("I am in the getQueueObservationMetricFallback fallback method with queue id {}", queueId);
        QueueObservationQueryResponse response = fallbackMetrics.get(queueId);

        return response;
    }

}
