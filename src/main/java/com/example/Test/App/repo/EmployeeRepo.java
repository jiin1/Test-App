package com.example.Test.App.repo;

import com.example.Test.App.dao.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Andrew Yantsen
 */

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    Optional<Employee> findEmployeeByUsername(String username);

}
