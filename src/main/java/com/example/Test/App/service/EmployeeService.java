package com.example.Test.App.service;

import com.example.Test.App.dao.Employee;

import java.util.Optional;

/**
 * @author Andrew Yantsen
 */

public interface EmployeeService {
    Optional<Employee> findEmployeeByUsername(String username);

    Boolean isPasswordValid(Long password, String username);

    String startWork(Employee employee);

    String finishWork(Employee employee);

    void create(Employee employee);


}
