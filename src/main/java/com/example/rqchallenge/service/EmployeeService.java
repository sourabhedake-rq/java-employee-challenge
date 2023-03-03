package com.example.rqchallenge.service;

import static com.example.rqchallenge.util.Constants.EMPLOYEES_URL;
import static com.example.rqchallenge.util.Constants.EMPLOYEE_CREATE;
import static com.example.rqchallenge.util.Constants.EMPLOYEE_DELETE;
import static com.example.rqchallenge.util.Constants.EMPLOYEE_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.exception.RqEmployeeNotFoundException;
import com.example.rqchallenge.restresponse.EmployeesResponse;
import com.example.rqchallenge.util.EmployeeComparator;
import com.example.rqchallenge.util.EmployeeConvertors;
import com.example.rqchallenge.util.Util;

/**
 * The service for the employee operations.
 * 
 * @author sedake
 *
 */
@Service
public class EmployeeService {

    /**
     * Maintains the retry timer to try the next request
     * 
     */
    public Integer nextRequestRetry;

    /**
     * The REST client to connect to External APIs
     * 
     */
    @Autowired
    private RestTemplate restTemplate;

    /**
     * The cache service object
     * 
     */
    @Autowired
    private EmployeeCacheService cache;

    public EmployeeService() {
        nextRequestRetry = 0;
    }

    /**
     * Get all the employees from the dummy server
     * 
     * @return the list of the employees
     */
    public List<Employee> getAllEmployee() {
        // GET /employees
        HttpEntity<Object> entity = new HttpEntity<>(null);
        ParameterizedTypeReference<EmployeesResponse<List<Employee>>> P = new ParameterizedTypeReference<EmployeesResponse<List<Employee>>>() {
        };
        ResponseEntity<EmployeesResponse<List<Employee>>> response = restTemplate.exchange(EMPLOYEES_URL,
                HttpMethod.GET, entity, P);
        if (response == null) {
            return null;
        }
        List<Employee> list = (List<Employee>) response.getBody().getData();
        cache.setAll(list);
        return list;
    }

    /**
     * Get the list of employees that match the name criteria.
     * 
     * @param name the name of the employee
     * @return the list of the employees
     */
    public List<Employee> getEmployeesByName(String name) {
        List<Employee> employees = getAllEmployee();
        employees = employees.stream().filter(emp -> emp.getEmployeeName().contains(name)).collect(Collectors.toList());
        if (employees.isEmpty()) {
            throw new RqEmployeeNotFoundException(name);
        }
        return employees;
    }

    /**
     * Get the employee by the identifier
     * 
     * @param id the identifier of the employee
     * @return the employee details
     */
    public Employee getEmployeeById(String id) {
        Integer intId = Integer.parseInt(id);
        Employee cached = cache.get(intId);
        if (cached != null) {
            return cached;
        }
        HttpEntity<Object> entity = new HttpEntity<>(null);
        ParameterizedTypeReference<EmployeesResponse<Employee>> P = new ParameterizedTypeReference<EmployeesResponse<Employee>>() {
        };
        ResponseEntity<EmployeesResponse<Employee>> response = restTemplate.exchange(Util.fill(EMPLOYEE_ID, id),
                HttpMethod.GET, entity, P);
        Employee result = (Employee) response.getBody().getData();
        if (result == null) {
            throw new RqEmployeeNotFoundException(intId);
        }
        cache.set(result.getId(), result);
        return result;
    }

    /**
     * Get the highest salary among all the employees
     * 
     * @return salary
     */
    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employees = getAllEmployee();
        Integer salary = -1;

        for (Employee emp : employees) {
            if (salary < emp.getEmployeeSalary()) {
                salary = emp.getEmployeeSalary();
            }
        }

        if (salary == -1) {
            throw new RqEmployeeNotFoundException();
        }

        return salary;
    }

    /**
     * Get the list of top ten highest earning employees
     * 
     * @return the name of top ten highest earning employees
     */
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = getAllEmployee();
        if (employees.isEmpty()) {
            throw new RqEmployeeNotFoundException();
        }
        List<String> names = new ArrayList<>();

        employees.sort(new EmployeeComparator());
        employees.subList(0, 10).stream().forEach(emp -> names.add(emp.getEmployeeName()));

        return names;
    }

    /**
     * Create the employee using the provided input parameters
     * 
     * @param employeeInput the map containing the inputs
     * @return the status of the employee creation
     */
    public String createEmployee(Map<String, Object> employeeInput) {
        Employee emp = EmployeeConvertors.convertToEmployee(employeeInput);
        HttpEntity<Object> entity = new HttpEntity<>(emp);
        ParameterizedTypeReference<EmployeesResponse<Employee>> P = new ParameterizedTypeReference<EmployeesResponse<Employee>>() {
        };

        ResponseEntity<EmployeesResponse<Employee>> result = restTemplate.exchange(EMPLOYEE_CREATE, HttpMethod.POST,
                entity, P);
        return result.getBody().getStatus();
    }

    /**
     * Delete the employee using the id
     * 
     * @param id the identifier
     * @return the name of the deleted employee
     */
    public String deleteEmployee(String id) {
        Employee emp = getEmployeeById(id);

        restTemplate.delete(Util.fill(EMPLOYEE_DELETE, id));
        cache.invalidate(emp.getId());
        return emp.getEmployeeName();
    }
}