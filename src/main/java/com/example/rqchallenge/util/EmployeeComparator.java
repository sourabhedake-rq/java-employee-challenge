package com.example.rqchallenge.util;

import java.util.Comparator;

import com.example.rqchallenge.dto.Employee;

/**
 * Comparator class used to sort the employees by the descending salaries
 * 
 * @author sedake
 *
 */
public class EmployeeComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee o1, Employee o2) {
        return -1 * o1.getEmployeeSalary().compareTo(o2.getEmployeeSalary());
    }

}
