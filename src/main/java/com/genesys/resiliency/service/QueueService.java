package com.genesys.resiliency.service;

import com.genesys.resiliency.exception.RetryException;
import com.genesys.resiliency.proxy.QueueServiceProxy;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.model.Queue;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.QueueObservationQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


@Service
public class QueueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueService.class);

    @Autowired
    private QueueServiceProxy queueServiceProxy;

    @Autowired
    private QueueServiceTimeoutFacade queueServiceTimeoutFacade;

    @Autowired
    private QueueServiceRetryFacade queueServiceRetryFacade;

    @Autowired
    private QueueServiceCircuitBreakerFacade queueServiceCircuitBreakerFacade;

    @Autowired
    private QueueServiceFallbackFacade queueServiceFallbackFacade;

    @Autowired
    private QueueServiceCacheFacade queueServiceCacheFacade;

    @Autowired
    private QueueServiceStackedFacade queueServiceStackedFacade;


    public QueueObservationQueryResponse getQueueObservationMetrics(String queueName) throws ApiException, RetryException, IOException {
        QueueEntityListing queueResult = queueServiceProxy.getQueueInfo(queueName);

        String queueId = null;

        if (queueResult.getPageCount()==1){
           Queue queue = queueResult.getEntities().get(0);
           queueId = queue.getId();

           return queueServiceProxy.getQueueObservationMetrics(queueId);
        } else {
            ApiException e = new ApiException(HttpStatus.NOT_FOUND.value(), "", new HashMap<String, String>(), "");
            throw e;
        }
    }


    public QueueObservationQueryResponse getQueueObservationMetricsWithCache(String queueName) throws ApiException, RetryException, IOException{
        QueueEntityListing queueResult = queueServiceCacheFacade.getQueueInfo(queueName);

        String queueId = null;

        if (queueResult.getPageCount()==1){
            Queue queue = queueResult.getEntities().get(0);
            queueId = queue.getId();

            return queueServiceCacheFacade.getQueueObservationMetrics(queueId);
        } else {
            ApiException e = new ApiException(HttpStatus.NOT_FOUND.value(), "", new HashMap<String, String>(), "");
            throw e;
        }
    }

    public QueueObservationQueryResponse getQueueObservationMetricsWithRetry(String queueName) throws ApiException, RetryException, IOException {
        QueueEntityListing queueResult = queueServiceRetryFacade.getQueueInfo(queueName);

        String queueId = null;

        if (queueResult.getPageCount()==1){
            Queue queue = queueResult.getEntities().get(0);
            queueId = queue.getId();

            return queueServiceRetryFacade.getQueueObservationMetrics(queueId);
        } else {
            ApiException e = new ApiException(HttpStatus.NOT_FOUND.value(), "", new HashMap<String, String>(), "");
            throw e;
        }
    }

    public QueueObservationQueryResponse getQueueObservationMetricsWithGenesysRetry(String queueName) throws ApiException, RetryException, IOException {
        QueueEntityListing queueResult = queueServiceProxy.getQueueInfo(queueName);

        String queueId = null;

        if (queueResult.getPageCount()==1){
            Queue queue = queueResult.getEntities().get(0);
            queueId = queue.getId();

            return queueServiceProxy.getQueueObservationMetrics(queueId);
        } else {
            ApiException e = new ApiException(HttpStatus.NOT_FOUND.value(), "", new HashMap<String, String>(), "");
            throw e;
        }
    }

    public QueueObservationQueryResponse getQueueObservationMetricsWithTimeout(String queueName) throws ApiException, IOException, RetryException, InterruptedException, ExecutionException {
        QueueEntityListing queueResult = queueServiceTimeoutFacade.getQueueInfo(queueName).get();

        String queueId = null;

        if (queueResult.getPageCount()==1){
            Queue queue = queueResult.getEntities().get(0);
            queueId = queue.getId();

            return queueServiceTimeoutFacade.getQueueObservationMetrics(queueId).get();
        } else {
            ApiException e = new ApiException(HttpStatus.NOT_FOUND.value(), "", new HashMap<String, String>(), "");
            throw e;
        }
    }

    public QueueObservationQueryResponse getQueueObservationMetricsWithCircuitBreaker(String queueName) throws ApiException,RetryException, IOException {
        QueueEntityListing queueResult = queueServiceCircuitBreakerFacade.getQueueInfo(queueName);

        String queueId = null;

        if (queueResult.getPageCount()==1){
            Queue queue = queueResult.getEntities().get(0);
            queueId = queue.getId();

            return queueServiceCircuitBreakerFacade.getQueueObservationMetrics(queueId);
        } else {
            ApiException e = new ApiException(HttpStatus.NOT_FOUND.value(), "", new HashMap<String, String>(), "");
            throw e;
        }
    }

    public QueueObservationQueryResponse getQueueObservationMetricsWithFallback(String queueName) throws ApiException,RetryException, IOException {
        QueueEntityListing queueResult = queueServiceFallbackFacade.getQueueInfo(queueName);

        String queueId = null;

        if (queueResult.getPageCount()==1){
            Queue queue = queueResult.getEntities().get(0);
            queueId = queue.getId();


            QueueObservationQueryResponse response =  queueServiceFallbackFacade.getQueueObservationMetrics(queueId);

            return response;
        } else {
            ApiException e = new ApiException(HttpStatus.NOT_FOUND.value(), "", new HashMap<String, String>(), "");
            throw e;
        }
    }

    public QueueObservationQueryResponse getQueueObservationMetricsStacked(String queueName) throws ApiException, IOException, RetryException, InterruptedException, ExecutionException {
        QueueEntityListing queueResult = queueServiceStackedFacade.getQueueInfo(queueName).get();

        String queueId = null;

        if (queueResult.getPageCount()==1){
            Queue queue = queueResult.getEntities().get(0);
            queueId = queue.getId();

            return queueServiceStackedFacade.getQueueObservationMetrics(queueId).get();
        } else {
            ApiException e = new ApiException(HttpStatus.NOT_FOUND.value(), "", new HashMap<String, String>(), "");
            throw e;
        }
    }
}
