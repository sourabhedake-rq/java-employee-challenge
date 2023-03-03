package com.example.rqchallenge.service;

import static com.example.rqchallenge.util.Constants.EMPLOYEES_URL;
import static com.example.rqchallenge.util.Constants.EMPLOYEE_CREATE;
import static com.example.rqchallenge.util.Constants.EMPLOYEE_DELETE;
import static com.example.rqchallenge.util.Constants.EMPLOYEE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.TestData;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.exception.RqBadEmployeeParameter;
import com.example.rqchallenge.exception.RqEmployeeNotFoundException;
import com.example.rqchallenge.restresponse.EmployeesResponse;
import com.example.rqchallenge.util.Util;

@SuppressWarnings("unchecked")
@SpringBootTest
public class EmployeeServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private EmployeeCacheService cache;

    @InjectMocks
    private EmployeeService empService = new EmployeeService();

    boolean initialised = false;

    @BeforeEach
    public void setupMocks() {
        if (initialised) {
            return;
        }
        initialised = true;

        TestData.init();

        Mockito.when(restTemplate.exchange(eq(EMPLOYEES_URL), eq(HttpMethod.GET), any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<EmployeesResponse<List<Employee>>>(TestData.allEmployeeResponse,
                        HttpStatus.OK));

        EmployeesResponse<Employee> employeeResp = new EmployeesResponse<>();
        employeeResp.setData(TestData.searchById);

        Mockito.when(restTemplate.exchange(eq(Util.fill(EMPLOYEE_ID, "" + TestData.searchById.getId())),
                eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<EmployeesResponse<Employee>>(employeeResp, HttpStatus.OK));

        EmployeesResponse<Employee> noEmployeeResp = new EmployeesResponse<>();
        Mockito.when(restTemplate.exchange(eq(Util.fill(EMPLOYEE_ID, "99")), eq(HttpMethod.GET), any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<EmployeesResponse<Employee>>(noEmployeeResp, HttpStatus.OK));

        EmployeesResponse<Employee> employeeCreateResp = new EmployeesResponse<>();
        employeeCreateResp.setStatus("success");

        Mockito.when(restTemplate.exchange(eq(EMPLOYEE_CREATE), eq(HttpMethod.POST), any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<EmployeesResponse<Employee>>(employeeCreateResp, HttpStatus.OK));

        Mockito.doNothing().when(restTemplate).delete(eq(Util.fill(EMPLOYEE_DELETE, "" + TestData.searchById.getId())));

        Mockito.when(restTemplate.exchange(eq(EMPLOYEES_URL), eq(HttpMethod.GET), any(HttpEntity.class),
                any(ParameterizedTypeReference.class)))
                .thenReturn(new ResponseEntity<EmployeesResponse<List<Employee>>>(TestData.allEmployeeResponse,
                        HttpStatus.OK))
                .thenReturn(new ResponseEntity<EmployeesResponse<List<Employee>>>(TestData.allEmployeeResponse,
                        HttpStatus.OK))
                .thenReturn(new ResponseEntity<EmployeesResponse<List<Employee>>>(TestData.allEmployeeResponse,
                        HttpStatus.OK))
                .thenThrow(HttpClientErrorException.class);

    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employee = empService.getAllEmployee();

        Mockito.verify(restTemplate).exchange(eq(EMPLOYEES_URL), eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));
        Assertions.assertEquals(TestData.allEmployeeResponse.getData(), employee);
    }

    @Test
    public void testGetEmployeesByNameSearch() {
        String nameToSearch = TestData.searchByName.get(0).getEmployeeName();
        List<Employee> employee = empService.getEmployeesByName(nameToSearch);

        Mockito.verify(restTemplate).exchange(eq(EMPLOYEES_URL), eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));
        Assertions.assertEquals(TestData.searchByName, employee);
    }

    @Test
    public void testGetHighestSalaryOfEmployees() {
        Integer highestSalary = TestData.highestSalariedEmployee.getEmployeeSalary();
        Integer actual = empService.getHighestSalaryOfEmployees();

        Mockito.verify(restTemplate).exchange(eq(EMPLOYEES_URL), eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));
        Assertions.assertEquals(highestSalary, actual);
    }

    @Test
    public void testGetEmployeesById() {
        String idToSearch = "" + TestData.searchById.getId();
        cache.invalidate(TestData.searchById.getId());
        Employee employee = empService.getEmployeeById(idToSearch);

        Mockito.verify(restTemplate).exchange(eq(Util.fill(EMPLOYEE_ID, "" + TestData.searchById.getId())),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class));
        Assertions.assertEquals(TestData.searchById, employee);
    }

    @Test
    public void testTopTenHighSalaryEmployee() {
        List<String> employees = empService.getTopTenHighestEarningEmployeeNames();

        Mockito.verify(restTemplate).exchange(eq(EMPLOYEES_URL), eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));
        Assertions.assertEquals(TestData.topTenEmployees, employees);
    }

    @Test
    public void testCreateEmployee() {
        Map<String, Object> input = new HashMap<>();
        input.put("name", TestData.newEmployee.getEmployeeName());
        input.put("age", TestData.newEmployee.getEmployeeAge());
        input.put("salary", TestData.newEmployee.getEmployeeSalary());

        String status = empService.createEmployee(input);

        Mockito.verify(restTemplate).exchange(eq(EMPLOYEE_CREATE), eq(HttpMethod.POST), any(),
                any(ParameterizedTypeReference.class));
        Assertions.assertEquals("success", status);
    }

    @Test
    public void testDeleteEmployee() {
        cache.invalidate(TestData.searchById.getId());
        String name = empService.deleteEmployee("" + TestData.searchById.getId());

        Mockito.verify(restTemplate).exchange(eq(Util.fill(EMPLOYEE_ID, "" + TestData.searchById.getId())),
                eq(HttpMethod.GET), any(), any(ParameterizedTypeReference.class));
        Mockito.verify(restTemplate).delete(eq(Util.fill(EMPLOYEE_DELETE, "" + TestData.searchById.getId())));
        Assertions.assertEquals(TestData.searchById.getEmployeeName(), name);
    }

    @Test()
    public void testGetNonExistingEmployee() {
        String idToSearch = "99";
        Exception ex1 = assertThrows(RqEmployeeNotFoundException.class, () -> empService.getEmployeeById(idToSearch));
        Mockito.verify(restTemplate).exchange(eq(Util.fill(EMPLOYEE_ID, "99")), eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));
        assertEquals("Employee with the id: [99] not found.", ex1.getMessage());

        String nameToSearch = "nonexisting";
        Exception ex2 = assertThrows(RqEmployeeNotFoundException.class,
                () -> empService.getEmployeesByName(nameToSearch));
        Mockito.verify(restTemplate).exchange(eq(Util.fill(EMPLOYEE_ID, "99")), eq(HttpMethod.GET), any(),
                any(ParameterizedTypeReference.class));
        assertEquals("Employee with the name: [nonexisting] not found.", ex2.getMessage());
    }

    @Test()
    public void testTooManyRequests() {
        empService.getAllEmployee();
        empService.getAllEmployee();
        empService.getAllEmployee();
        assertThrows(HttpClientErrorException.class, () -> empService.getAllEmployee());
    }

    @Test()
    public void testBadParameterEmployee() {
        Map<String, Object> input = new HashMap<>();
        Exception ex1 = assertThrows(RqBadEmployeeParameter.class, () -> empService.createEmployee(input));
        Assertions.assertEquals("Bad Parameter for [name]. Verify if it is provided in the request.", ex1.getMessage());

        input.put("name", "name");
        Exception ex2 = assertThrows(RqBadEmployeeParameter.class, () -> empService.createEmployee(input));
        Assertions.assertEquals("Bad Parameter for [age]. Verify if it is provided in the request.", ex2.getMessage());

        input.put("age", 17);
        Exception ex3 = assertThrows(RqBadEmployeeParameter.class, () -> empService.createEmployee(input));
        Assertions.assertEquals("Bad Parameter for [salary]. Verify if it is provided in the request.",
                ex3.getMessage());

        input.put("name", new Employee());
        Exception ex4 = assertThrows(RqBadEmployeeParameter.class, () -> empService.createEmployee(input));
        Assertions.assertEquals("Bad Parameter for [name]. Expected <String> but got <Employee>.", ex4.getMessage());

        input.put("name", "test");
        input.put("age", new String());
        Exception ex5 = assertThrows(RqBadEmployeeParameter.class, () -> empService.createEmployee(input));
        Assertions.assertEquals("Bad Parameter for [age]. Expected <Integer> but got <String>.", ex5.getMessage());

        input.put("age", 21);
        input.put("salary", new Object());
        Exception ex6 = assertThrows(RqBadEmployeeParameter.class, () -> empService.createEmployee(input));
        Assertions.assertEquals("Bad Parameter for [salary]. Expected <Integer> but got <Object>.", ex6.getMessage());
    }
}