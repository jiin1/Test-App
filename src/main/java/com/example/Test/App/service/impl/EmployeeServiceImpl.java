package com.example.Test.App.service.impl;

import com.example.Test.App.dao.Employee;
import com.example.Test.App.dao.WorkingTime;
import com.example.Test.App.repo.EmployeeRepo;
import com.example.Test.App.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;



    public EmployeeServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;

    }


    @Override
    public Optional<Employee> findEmployeeByUsername(String username) {
        return employeeRepo.findEmployeeByUsername(username);
    }


    @Override
    public Boolean isPasswordValid(Long password, String username) {
        Optional<Employee> opti = employeeRepo.findEmployeeByUsername(username);
        if (opti.isPresent()) {
            return opti.get().getPassword().equals(password);
        } else return null;
    }

    @Override
    public String startWork(Employee employee) {
        employee.getWorkingTimeList().add(new WorkingTime());
        WorkingTime wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
        wor.setStartTime(LocalDateTime.now());
        employee.setActive(true);
        employeeRepo.save(employee);
        return "Your work is started on " + employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1).getStartTime();
    }

    @Override
    public String finishWork(Employee employee) {
        employee.setActive(false);
        WorkingTime wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
        wor.setFinishTime(LocalDateTime.now());
        employeeRepo.saveAndFlush(employee);
        return "Your work is finished on " + employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1).getFinishTime();
    }

    @Override
    public void create(Employee employee) {
        employee.setCreationDate(LocalDateTime.now());
        employee.setActive(false);
        employeeRepo.save(employee);
    }
}
