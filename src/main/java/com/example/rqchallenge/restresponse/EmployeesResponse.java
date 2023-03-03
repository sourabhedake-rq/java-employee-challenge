package com.example.rqchallenge.restresponse;

/**
 * The response of the employee API
 * 
 * @author sedake
 *
 * @param <T> Generic data type. This may contain the single employee or the
 *            list of employees
 */
public class EmployeesResponse<T> {

    /**
     * The status of the operation
     * 
     */
    private String status;

    /**
     * The employee details
     * 
     */
    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
