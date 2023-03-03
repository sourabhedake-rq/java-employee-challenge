package com.example.rqchallenge.exception;

/**
 * This exception is thrown when we do not find the employee
 * 
 * @author sedake
 *
 */
public class RqEmployeeNotFoundException extends RuntimeException {

    /**
     * The name of the employee
     * 
     */
    private String name;

    /**
     * The id of the employee
     * 
     */
    private Integer id;

    /**
     * The default constructor
     * 
     */
    public RqEmployeeNotFoundException() {
        name = "";
        id = -1;
    }

    /**
     * The constructor that accepts the name of the employee
     * 
     * @param name The name of the employee
     */
    public RqEmployeeNotFoundException(String name) {
        this.name = name;
        this.id = -1;
    }

    /**
     * The constructor that accepts the id of the employee
     * 
     * @param id The identifier of the employee
     */
    public RqEmployeeNotFoundException(Integer id) {
        this.id = id;
        this.name = "";
    }

    /**
     * Get the name of the employee
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the employee
     * 
     * @param name The name of the employee
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * The ID of the employee
     * 
     * @return the identifier
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the id of the employee
     * 
     * @param id the identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the human readable message
     * 
     * @return the message
     */
    public String getMessage() {
        StringBuilder msg = new StringBuilder();
        if (name.isEmpty() && id != -1) {
            msg.append("Employee with the id: [").append(id).append("] not found.");
        } else if (!name.isEmpty() && id == -1) {
            msg.append("Employee with the name: [").append(name).append("] not found.");
        } else {
            msg.append("There are no employees to work on the request.");
        }
        return msg.toString();
    }
}
