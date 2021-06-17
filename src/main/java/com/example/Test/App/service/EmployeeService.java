package com.example.Test.App.service;

import com.example.Test.App.dao.Employee;

import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> findEmployeeByUsername(String username);
    Boolean isPasswordValid(Long password, String username);
    String startWork(Employee employee);
    String finishWork(Employee  employee);
    Employee create (Employee employee);




}
