package com.example.rqchallenge.exception;

import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import com.example.rqchallenge.dto.ErrorException;

/**
 * This class returns the customiZed common exception for the various exceptions
 * 
 * @author sedake
 *
 */
@ControllerAdvice
public class RqException {
    private static final Logger logger = Logger.getLogger(RqException.class.getSimpleName());

    /**
     * Catch the exceptions from REST client
     * 
     * @param exception The thrown exception
     * @return The customized exception
     */
    @ExceptionHandler(value = HttpClientErrorException.class)
    public ResponseEntity<ErrorException> exception(HttpClientErrorException exception) {
        logger.severe(exception.getMessage());
        String retrySeconds = null;
        if (exception.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
            // Let's figure out after how much time we can request again
            retrySeconds = exception.getResponseHeaders().getFirst("retry-after");
            logger.info("Retry the request in " + retrySeconds + " seconds.");
        }
        return new ResponseEntity<ErrorException>(new ErrorException(exception.getMessage(), retrySeconds),
                exception.getStatusCode());
    }

    @ExceptionHandler(value = RqEmployeeNotFoundException.class)
    public ResponseEntity<ErrorException> exception(RqEmployeeNotFoundException exception) {
        logger.severe(exception.getMessage());
        return new ResponseEntity<>(new ErrorException(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = RqBadEmployeeParameter.class)
    public ResponseEntity<ErrorException> exception(RqBadEmployeeParameter exception) {
        logger.severe(exception.getMessage());
        return new ResponseEntity<>(new ErrorException(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}