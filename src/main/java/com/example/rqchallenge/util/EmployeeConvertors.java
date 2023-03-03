package com.example.rqchallenge.util;

import java.util.Map;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.exception.RqBadEmployeeParameter;

/**
 * Convertor class
 * 
 * @author sedake
 *
 */
public class EmployeeConvertors {

    /**
     * Convert the input map to the employee object
     * 
     * @param employeeInput the input map
     * @return the employee object
     */
    public static Employee convertToEmployee(Map<String, Object> employeeInput) {
        Employee emp = new Employee();
        if (employeeInput.containsKey("name")) {
            Object obj = employeeInput.get("name");
            if (!(obj instanceof String)) {
                throw new RqBadEmployeeParameter("name", String.class.getSimpleName(), obj.getClass().getSimpleName());
            }
            emp.setEmployeeName((String) employeeInput.get("name"));
        } else {
            throw new RqBadEmployeeParameter("name");
        }

        if (employeeInput.containsKey("age")) {
            Object obj = employeeInput.get("age");
            if (!(obj instanceof Integer)) {
                throw new RqBadEmployeeParameter("age", Integer.class.getSimpleName(), obj.getClass().getSimpleName());
            }
            emp.setEmployeeAge((Integer) employeeInput.get("age"));
        } else {
            throw new RqBadEmployeeParameter("age");
        }

        if (employeeInput.containsKey("salary")) {
            Object obj = employeeInput.get("salary");
            if (!(obj instanceof Integer)) {
                throw new RqBadEmployeeParameter("salary", Integer.class.getSimpleName(),
                        obj.getClass().getSimpleName());
            }
            emp.setEmployeeSalary((Integer) employeeInput.get("salary"));
        } else {
            throw new RqBadEmployeeParameter("salary");
        }

        return emp;
    }

}
