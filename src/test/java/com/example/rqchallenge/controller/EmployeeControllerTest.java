package com.example.rqchallenge.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import com.example.rqchallenge.RqChallengeApplicationTests;
import com.example.rqchallenge.TestData;
import com.example.rqchallenge.dto.Employee;
import com.example.rqchallenge.exception.RqEmployeeNotFoundException;
import com.example.rqchallenge.service.EmployeeService;

@SpringBootTest
@TestMethodOrder(MethodOrderer.MethodName.class)
public class EmployeeControllerTest extends RqChallengeApplicationTests {

    @Mock
    EmployeeService service;

    @InjectMocks
    EmployeeController controller;

    private static MockMvc mvc;
    boolean initialised = false;

    @BeforeEach
    public void setupMocks() {
        if (initialised) {
            return;
        }
        initialised = true;
        MockitoAnnotations.openMocks(this);

        mvc = MockMvcBuilders.standaloneSetup(controller).build();
        TestData.init();
    }

    @Test
    public void getAllEmployees() throws Exception {
        Mockito.when(service.getAllEmployee()).thenReturn(TestData.allEmployees);

        String uri = "/";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);
        String content = mvcResult.getResponse().getContentAsString();
        Employee[] employees = super.mapFromJson(content, Employee[].class);
        assertTrue(employees.length > 0);
    }

    @Test
    public void getNotExistingEmployeeByName() throws Exception {
        Mockito.when(service.getAllEmployee()).thenReturn(TestData.allEmployees);
        Mockito.when(service.getEmployeesByName(eq("Nexus"))).thenThrow(new RqEmployeeNotFoundException("Nexus"));

        String uri = "/search/Nexus";
        Exception ex = assertThrows(NestedServletException.class, () -> mvc
                .perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE)).andReturn());
        assertEquals(RqEmployeeNotFoundException.class, ex.getCause().getClass());
    }

    @Test
    public void getEmployeebyName() throws Exception {
        Mockito.when(service.getAllEmployee()).thenReturn(TestData.allEmployees);
        Mockito.when(service.getEmployeesByName(eq("Nixon"))).thenReturn(TestData.searchByName);

        String uri = "/search/Nixon";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String content = mvcResult.getResponse().getContentAsString();
        Employee[] employees = super.mapFromJson(content, Employee[].class);
        assertTrue(employees.length > 0);
    }

    @Test
    public void getEmployeebyId() throws Exception {
        Mockito.when(service.getEmployeeById(eq("9"))).thenReturn(TestData.searchById);

        String uri = "/9";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String content = mvcResult.getResponse().getContentAsString();
        Employee employee = super.mapFromJson(content, Employee.class);
        assertEquals(9, employee.getId());
    }

    @Test
    public void getHighestSalary() throws Exception {
        Mockito.when(service.getHighestSalaryOfEmployees())
                .thenReturn(TestData.highestSalariedEmployee.getEmployeeSalary());

        String uri = "/highestSalary";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String content = mvcResult.getResponse().getContentAsString();
        Integer salary = super.mapFromJson(content, Integer.class);
        assertEquals(91000, salary);
    }

    @Test
    public void getTopTenHighestSalaryEmployees() throws Exception {
        Mockito.when(service.getTopTenHighestEarningEmployeeNames()).thenReturn(TestData.topTenEmployees);

        String uri = "/topTenHighestEarningEmployeeNames";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String content = mvcResult.getResponse().getContentAsString();
        String[] employees = super.mapFromJson(content, String[].class);
        assertEquals(employees.length, 10);
    }

    @Test
    public void testCreateEmployee() throws Exception {
        Mockito.when(service.createEmployee(any())).thenReturn("success");

        String uri = "/";
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri).content("{\"name\":\"S Edake\", \"age\": 21, \"salary\":233}")
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("success", content);
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Mockito.when(service.deleteEmployee(any())).thenReturn(TestData.searchById.getEmployeeName());

        String uri = "/9";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri).accept(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value(), status);

        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("CAPNAME", content);
    }

}