package com.genesys.resiliency.controllers;

import com.genesys.resiliency.exception.RetryException;
import com.genesys.resiliency.service.QueueService;
import com.mypurecloud.sdk.v2.ApiException;
import com.mypurecloud.sdk.v2.model.QueueObservationQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(value = "/v1/resiliency")
public class ResiliencyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResiliencyController.class);

    @Autowired
    QueueService queueService;

    @RequestMapping(value = "/queue/{queueName}", method = RequestMethod.GET)
    public ResponseEntity<QueueObservationQueryResponse> lookupQueueInfo(@PathVariable("queueName") String queueName,
            @RequestParam(value = "type", defaultValue = "normal") Optional<String> type) {
        try {
            if (!type.isPresent() || type.get().equalsIgnoreCase("normal")) {
                return new ResponseEntity<>(queueService.getQueueObservationMetrics(queueName), HttpStatus.OK);
            }

            if (type.get().equalsIgnoreCase("cache")) {
                return new ResponseEntity<>(queueService.getQueueObservationMetricsWithCache(queueName), HttpStatus.OK);
            }

            if (type.get().equalsIgnoreCase("retry")) {
                return new ResponseEntity<>(queueService.getQueueObservationMetricsWithRetry(queueName), HttpStatus.OK);
            }

            if (type.get().equalsIgnoreCase("timeout")) {
                return new ResponseEntity<>(queueService.getQueueObservationMetricsWithTimeout(queueName),
                        HttpStatus.OK);
            }

            if (type.get().equalsIgnoreCase("circuitbreaker")) {
                return new ResponseEntity<>(queueService.getQueueObservationMetricsWithCircuitBreaker(queueName),
                        HttpStatus.OK);
            }

            if (type.get().equalsIgnoreCase("fallback")) {
                return new ResponseEntity<>(queueService.getQueueObservationMetricsWithFallback(queueName),
                        HttpStatus.OK);
            }

            if (type.get().equalsIgnoreCase("stacked")) {
                return new ResponseEntity<>(queueService.getQueueObservationMetricsStacked(queueName), HttpStatus.OK);
            }
        } catch (RetryException e) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, e.getRawBody());
        } catch (ApiException e) {
            LOGGER.info("STATUS CODE {}", e.getStatusCode());
            throw new ResponseStatusException(HttpStatus.resolve(e.getStatusCode()), e.getRawBody());
        } catch (Exception ex) {
            LOGGER.info("I am in the Exception phase {}", ex.getStackTrace());
            // ex.printStackTrace();
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.toString());
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}