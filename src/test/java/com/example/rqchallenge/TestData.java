package com.example.rqchallenge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.restresponse.EmployeesResponse;

public class TestData {

    public static EmployeesResponse<List<Employee>> allEmployeeResponse;
    public static List<Employee> allEmployees;
    public static Employee searchById;
    public static Employee highestSalariedEmployee;
    public static Employee newEmployee;
    public static List<Employee> searchByName;
    public static List<String> topTenEmployees = Arrays.asList(new String[] { "!@#$%^&*()", "Mr Name", "name",
            "another name", "symbol@name", "test name", "1234 name", "no name", "ok_name", "my name" });

    public static void init() {
        allEmployees = new ArrayList<>();
        allEmployeeResponse = new EmployeesResponse<>();
        searchByName = new ArrayList<>();

        allEmployees.add(new Employee(1, "my name", 1000, 21));
        allEmployees.add(new Employee(3, "no name", 2000, 21));
        allEmployees.add(new Employee(4, "another name", 8000, 28));
        allEmployees.add(new Employee(5, "name", 9000, 24));
        allEmployees.add(new Employee(6, "1234 name", 3000, 28));
        allEmployees.add(new Employee(7, "ok_name", 2000, 99));
        allEmployees.add(new Employee(8, "symbol@name", 6000, 0));
        allEmployees.add(new Employee(11, "Mr Name", 15000, 12));

        searchByName.add(new Employee(2, "test name", 5000, 22));
        searchById = new Employee(9, "CAPNAME", 100, 2);
        highestSalariedEmployee = new Employee(10, "!@#$%^&*()", 91000, 1);
        allEmployees.add(highestSalariedEmployee);
        allEmployees.add(searchByName.get(0));
        allEmployees.add(searchById);
        newEmployee = new Employee(14, "New_Name", 1500, 19);
        allEmployeeResponse.setData(allEmployees);
    }
}
