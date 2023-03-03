package com.example.rqchallenge.dto;

import com.example.rqchallenge.util.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * The common error exception DTO. It returns the error message, retry variable
 * and the version string.
 * 
 * @author sedake
 *
 */
@JsonInclude(Include.NON_NULL)
public class ErrorException {
    /**
     * This is the first version :) In production, this can be useful to debug any
     * reported bugs
     * 
     */
    private static String version = Constants.VERSION;

    /**
     * The error message
     * 
     */
    private String errMessage;

    /**
     * The retryAfter variable after which next API should be called (seconds)
     * 
     */
    private Integer retryAfterSeconds;

    /**
     * Default constructor
     * 
     */
    public ErrorException() {
    }

    /**
     * Constructor with the message
     * 
     * @param errMessage The error message
     */
    public ErrorException(String errMessage) {
        this(errMessage, null);
    }

    /**
     * The constructor that takes the error message and retry after variable
     * 
     * @param errMessage        The error message
     * @param retryAfterSeconds The time in seconds after which the API should be
     *                          retried
     */
    public ErrorException(String errMessage, String retryAfterSeconds) {
        this.errMessage = errMessage;
        if (retryAfterSeconds != null) {
            this.retryAfterSeconds = Integer.parseInt(retryAfterSeconds);
        }
    }

    /**
     * Get the error message
     * 
     * @return The error message
     */
    public String getErrMessage() {
        return errMessage;
    }

    /**
     * Set the error message
     * 
     * @param errMessage The error message
     */
    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    /**
     * Get the time for retry
     * 
     * @return The time in seconds
     */
    public Integer getRetryAfterSeconds() {
        return retryAfterSeconds;
    }

    /**
     * Sets the time for retry
     * 
     * @param retryAfterSeconds The time in seconds
     */
    public void setRetryAfterSeconds(Integer retryAfterSeconds) {
        this.retryAfterSeconds = retryAfterSeconds;
    }

    /**
     * The current version of the application
     * 
     * @return the application version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the current version
     * 
     * @param version Version of the application
     */
    public void setVersion(String version) {
        ErrorException.version = version;
    }
}
