package com.genesys.resiliency.service;

import com.genesys.resiliency.proxy.QueueServiceProxy;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.model.QueueEntityListing;
import com.mypurecloud.sdk.v2.model.QueueObservationQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;

/*
 * This class implements the Cache Caffeine logic.  Configuration for the Caffeine cache Configuration for the @Cacheable annotation can be found in the src/main/resources/application.yml file.
 * You can test this code by running src/python/getMetrics.py script with the cache parameter (e.g. getMetrics.py cache).  That will hit this
 * specific code.
 */
@Service
public class QueueServiceCacheFacade {

    @Autowired
    private QueueServiceProxy queueServiceProxy;

    @Cacheable(cacheNames = "queueMetricCache")
    public QueueObservationQueryResponse getQueueObservationMetrics(String queueId) throws ApiException, IOException {
        return queueServiceProxy.getQueueObservationMetrics(queueId);
    }

    @Cacheable(cacheNames = "queueCache")
    public QueueEntityListing getQueueInfo(String queueName) throws IOException, ApiException {
        return queueServiceProxy.getQueueInfo(queueName);
    }

}
