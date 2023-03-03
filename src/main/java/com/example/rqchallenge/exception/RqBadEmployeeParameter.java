package com.example.rqchallenge.exception;

/**
 * This exception is thrown when any of input parameter is not in the expected
 * format.
 * 
 * @author sedake
 *
 */
public class RqBadEmployeeParameter extends RuntimeException {
    /**
     * The parameter name for the input is bad
     * 
     */
    private String parameter;

    /**
     * The expected type of the parameter
     * 
     */
    private String expectedClass;

    /**
     * The actual type of the parameter
     * 
     */
    private String actualClass;

    /**
     * Constructor for the parameter
     * 
     * @param parameter The name of the input parameter
     */
    public RqBadEmployeeParameter(String parameter) {
        this(parameter, "", "");
    }

    /**
     * Constructor for the parameter with it's exception and actual type
     * 
     * @param parameter     The name of the input parameter
     * @param expectedClass The expected type of the parameter
     * @param actualClass   The actual type of the parameter
     */
    public RqBadEmployeeParameter(String parameter, String expectedClass, String actualClass) {
        this.parameter = parameter;
        this.expectedClass = expectedClass;
        this.actualClass = actualClass;
    }

    /**
     * Get the parameter
     * 
     * @return The name of the parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Set the parameter name
     * 
     * @param parameter the name of the parameter
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * Get the expected type of the parameter
     * 
     * @return the expected type of parameter
     */
    public String getExpectedClass() {
        return expectedClass;
    }

    /**
     * Set the expected type of the parameter
     * 
     * @param expectedClass The type of the parameter
     */
    public void setExpectedClass(String expectedClass) {
        this.expectedClass = expectedClass;
    }

    /**
     * Get the actual type of the parameter
     * 
     * @return the actual type of the parameter
     */
    public String getActualClass() {
        return actualClass;
    }

    /**
     * Set the actual type of the parameter
     * 
     * @param actualClass the actual type of the parameter
     */
    public void setActualClass(String actualClass) {
        this.actualClass = actualClass;
    }

    /**
     * Get the human readable message
     * 
     * @return the message
     */
    public String getMessage() {
        if (expectedClass.isEmpty() && actualClass.isEmpty()) {
            return "Bad Parameter for [" + getParameter() + "]. Verify if it is provided in the request.";
        }
        return "Bad Parameter for [" + getParameter() + "]. Expected <" + getExpectedClass() + "> but got <"
                + getActualClass() + ">.";
    }
}
