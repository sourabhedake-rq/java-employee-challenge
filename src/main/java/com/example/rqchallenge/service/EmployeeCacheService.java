package com.example.rqchallenge.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.rqchallenge.dto.Employee;

/**
 * The cache service that caches the employee data. This can be improved further
 * to maintain the employee hits.
 * 
 * @author sedake
 *
 */
@Service
public class EmployeeCacheService {

    /**
     * This will keep the employee data once they are fetched from the server
     * 
     */
    Map<Integer, Employee> cache;

    public EmployeeCacheService() {
        cache = new HashMap<>();
    }

    public Employee get(Integer id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        return null;
    }

    public void set(Integer id, Employee employee) {
        cache.put(id, employee);
    }

    public void setAll(List<Employee> employees) {
        employees.stream().forEach(emp -> set(emp.getId(), emp));
    }

    public void invalidate(Integer id) {
        cache.remove(id);
    }
}
