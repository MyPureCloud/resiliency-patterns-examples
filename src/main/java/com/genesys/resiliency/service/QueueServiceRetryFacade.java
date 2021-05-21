package com.genesys.resiliency.service;

import com.genesys.resiliency.exception.RetryException;
import com.genesys.resiliency.proxy.QueueServiceProxy;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.QueueObservationQueryResponse;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class QueueServiceRetryFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceRetryFacade.class);

    @Autowired
    QueueServiceProxy queueServiceProxy;



   // @Retry(name="genesysretry")
    public QueueObservationQueryResponse getQueueObservationMetrics(String queueId) throws ApiException, RetryException, IOException {
        return queueServiceProxy.getQueueObservationMetrics(queueId);
    }

    //@Retry(name="genesysretry")
    public QueueEntityListing getQueueInfo(String queueName) throws IOException, RetryException, ApiException{
       return queueServiceProxy.getQueueInfo(queueName);

    }
}
