package com.genesys.resiliency.exception;


import com.mypurecloud.sdk.v2.ApiException;

import java.util.Map;

/*
   The RetryException class is used to differentiate RetryableExceptions from non-retryable exceptions.
 */
public class RetryException extends ApiException {

    public RetryException(int statusCode, String message, Map<String, String> headers, String body) {
        super(statusCode, message, headers, body);
    }

    public RetryException(Throwable cause) {
        super(cause);
    }
}
