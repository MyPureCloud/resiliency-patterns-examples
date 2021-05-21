package com.genesys.resiliency.proxy;

import com.genesys.resiliency.exception.RetryException;
import com.genesys.resiliency.service.QueueServiceRetryFacade;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.api.RoutingApi;
import com.mypurecloud.sdk.v2.api.request.GetRoutingQueuesRequest;
import com.mypurecloud.sdk.v2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class QueueServiceProxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceRetryFacade.class);

    private void resolveRetryException(ApiException e) throws RetryException, ApiException{
        if (e.getStatusCode()==429 ||
                e.getStatusCode()==502 ||
                e.getStatusCode()==503 ||
                e.getStatusCode()==504) {
            LOGGER.info("A retryable exception has occurred. Resilience4j will retry based on defined retry policy.");
            throw new RetryException(e);
        }

        throw e;
    }

    public QueueObservationQueryResponse getQueueObservationMetrics(String queueId) throws ApiException, RetryException, IOException {
        RoutingApi routingAPI = new RoutingApi();
        QueueObservationQuery query = new QueueObservationQuery();

        //Setup my predicates
        QueueObservationQueryPredicate predicate = new QueueObservationQueryPredicate();
        predicate.setDimension(QueueObservationQueryPredicate.DimensionEnum.QUEUEID);
        predicate.setOperator(QueueObservationQueryPredicate.OperatorEnum.MATCHES);
        predicate.setValue(queueId);
        List<QueueObservationQueryPredicate> predicates = new ArrayList<>();
        predicates.add(predicate);

        QueueObservationQueryFilter filter = new QueueObservationQueryFilter();
        filter.setType(QueueObservationQueryFilter.TypeEnum.OR);
        filter.setPredicates(predicates);

        List<QueueObservationQuery.MetricsEnum> metrics= new ArrayList<>();
        metrics.add(QueueObservationQuery.MetricsEnum.OACTIVEUSERS);
        metrics.add(QueueObservationQuery.MetricsEnum.OINTERACTING);
        metrics.add(QueueObservationQuery.MetricsEnum.OMEMBERUSERS);
        metrics.add(QueueObservationQuery.MetricsEnum.OOFFQUEUEUSERS);
        metrics.add(QueueObservationQuery.MetricsEnum.OONQUEUEUSERS);
        metrics.add(QueueObservationQuery.MetricsEnum.OUSERPRESENCES);
        metrics.add(QueueObservationQuery.MetricsEnum.OUSERPRESENCES);
        metrics.add(QueueObservationQuery.MetricsEnum.OUSERROUTINGSTATUSES);
        metrics.add(QueueObservationQuery.MetricsEnum.OUSERPRESENCES);

        query.setFilter(filter);
        query.setMetrics(metrics);

        try {
            return routingAPI.postAnalyticsQueuesObservationsQuery(query);
        }catch(ApiException e){
            resolveRetryException(e);
        }
        return null;
    }



    public QueueEntityListing getQueueInfo(String queueName) throws IOException, RetryException, ApiException {
        RoutingApi routingApi = new RoutingApi();
        GetRoutingQueuesRequest request = new GetRoutingQueuesRequest();
        request.setName(queueName);

        try {
            return routingApi.getRoutingQueues(request);
        } catch(ApiException e){
            resolveRetryException(e);
        }

        return null;
    }
}
