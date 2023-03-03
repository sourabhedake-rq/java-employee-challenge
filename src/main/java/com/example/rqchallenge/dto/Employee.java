package com.example.rqchallenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for the Employee. This maintains all the attributes.
 * 
 * @author sedake
 *
 */
@JsonInclude(Include.NON_NULL)
public class Employee {

    /**
     * The identifier of the employee.
     * 
     */
    private Integer id;

    /**
     * The name of the employee.
     * 
     */
    @JsonProperty("employee_name")
    private String employeeName;

    /**
     * The salary of the employee.
     * 
     */
    @JsonProperty("employee_salary")
    private Integer employeeSalary;

    /**
     * The age of the employee.
     * 
     */
    @JsonProperty("employee_age")
    private Integer employeeAge;

    /**
     * The default constructor that sets null values
     * 
     */
    public Employee() {
        this(null, null, null, null);
    }

    /**
     * The constructor to set all the class fields
     * 
     * @param id             the identifier of the employee
     * @param employeeName   the name of the employee
     * @param employeeSalary the salary of the employee
     * @param employeeAge    the age of the employee
     */
    public Employee(Integer id, String employeeName, Integer employeeSalary, Integer employeeAge) {
        this.id = id;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
        this.employeeAge = employeeAge;
    }

    /**
     * Get the identifier of the employee
     * 
     * @return identifier
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the identifier of the employee
     * 
     * @param id identifier
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the employee name
     * 
     * @return name
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Set the employee name
     * 
     * @param employeeName name of the employee
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * Get the salary of the employee
     * 
     * @return salary
     */
    public Integer getEmployeeSalary() {
        return employeeSalary;
    }

    /**
     * Set the salary of the employee
     * 
     * @param employeeSalary salary
     */
    public void setEmployeeSalary(Integer employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    /**
     * Get the employee age
     * 
     * @return age
     */
    public Integer getEmployeeAge() {
        return employeeAge;
    }

    /**
     * Set the employee age
     * 
     * @param employeeAge age of the employee
     */
    public void setEmployeeAge(Integer employeeAge) {
        this.employeeAge = employeeAge;
    }

}
