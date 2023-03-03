package com.example.rqchallenge.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.service.EmployeeService;

/**
 * Controller for the employee APIs
 * 
 * @author sedake
 *
 */
@RestController
public class EmployeeController implements IEmployeeController {

    private static final Logger logger = Logger.getLogger(EmployeeController.class.getSimpleName());

    @Autowired
    RestTemplate restClient;

    @Autowired
    EmployeeService employeeService;

    /**
     * Get all the employees from the web service
     * 
     * @return list of employees
     */
    @GetMapping()
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        logger.finest("Entered getAllEmployees");

        List<Employee> employees = employeeService.getAllEmployee();
        logger.info("Found " + employees.size() + " employees");
        return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
    }

    /**
     * Search the employees by name
     * 
     * @return list of employees
     */
    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        logger.finest("Entered getEmployeesByNameSearch");
        List<Employee> emp = employeeService.getEmployeesByName(searchString);
        logger.info("Found " + emp.size() + " employees with name [" + searchString + "]");
        return new ResponseEntity<List<Employee>>(emp, HttpStatus.OK);
    }

    /**
     * Get the employee by id
     * 
     * @return the employee
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        logger.finest("Entered getEmployeeById");
        Employee emp = employeeService.getEmployeeById(id);
        logger.info("Returning the details for the employee: [" + emp.getEmployeeName() + "]");
        return new ResponseEntity<Employee>(emp, HttpStatus.OK);
    }

    /**
     * Get the salary of the highest earning employee
     * 
     * @return the salary
     */
    @GetMapping("/highestSalary")
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        logger.finest("Entered getHighestSalaryOfEmployees");
        Integer salary = employeeService.getHighestSalaryOfEmployees();
        logger.info("Returned the highest salary of the employee");
        return new ResponseEntity<Integer>(salary, HttpStatus.OK);
    }

    /**
     * Get the names of the top ten highest earning employees
     * 
     * @return the name of the employees
     */
    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.finest("Entered getTopTenHighestEarningEmployeeNames");
        List<String> topTen = employeeService.getTopTenHighestEarningEmployeeNames();
        logger.info("Returned the details of the top ten highest earning employess");
        return new ResponseEntity<List<String>>(topTen, HttpStatus.OK);
    }

    /**
     * Create the employee using the input parameters. This requires the employee
     * name, age and salary of the employee
     * 
     * @return the status of the operation
     */
    @PostMapping()
    public ResponseEntity<String> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        logger.finest("Entered createEmployee");
        String status = employeeService.createEmployee(employeeInput);
        logger.info("Created the employee record for the " + (String) employeeInput.get("name"));
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    /**
     * Delete the employee by provided id
     * 
     * @return the name of the deleted employee
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        logger.finest("Entered deleteEmployeeById");
        String name = employeeService.deleteEmployee(id);
        logger.info("Deleted the employee record for the " + name + " (" + id + ")");
        return new ResponseEntity<>(name, HttpStatus.OK);
    }
}
