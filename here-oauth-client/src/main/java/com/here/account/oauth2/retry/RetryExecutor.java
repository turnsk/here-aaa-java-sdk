package com.here.account.oauth2.retry;

import com.here.account.http.HttpProvider;

import java.util.logging.Logger;

/**
 * A {@code RetryExecutor} provides a mechanisms to execute a {@link Retryable}
 * and will retry on failure according to an implementation specific retry policy.
 * */
public class RetryExecutor {

    private final RetryPolicy retryPolicy;
    private static final Logger LOGGER = Logger.getLogger(RetryExecutor.class.getName());

    public RetryExecutor(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    /**
     * Execute the given {@link Retryable} until retry policy decides to give up.
     * @param retryable the {@link Retryable} to execute
     * @return http response return from {@code Retryable}
     * @throws Exception
     */
    public HttpProvider.HttpResponse execute(Retryable retryable) throws Exception {
        RetryContext retryContext = new RetryContext();
        HttpProvider.HttpResponse httpResponse;

        while (true) {
            try {
                httpResponse = retryable.execute();
                retryContext.setLastRetryResponse(httpResponse);
            } catch (Exception e) {
                retryContext.setLastException(e);
            }

            if (retryPolicy.shouldRetry(retryContext)) {
                retryContext.incrementRetryCount();

                int waitInterval = retryPolicy.getNextRetryIntervalMillis(retryContext);

                LOGGER.warning("Retrying after - "+ waitInterval +" milliseconds...");
                try {
                    Thread.sleep(waitInterval);
                } catch (InterruptedException e){
                    LOGGER.warning("Got InterruptedException while waiting to retry.");
                }
            } else {
                break;
            }
        }

        if (retryContext.getLastException() != null) {
            throw retryContext.getLastException();
        }

        return retryContext.getLastRetryResponse();
    }
}
