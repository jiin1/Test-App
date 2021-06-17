package com.example.Test.App.Controller;

import com.example.Test.App.dao.Employee;
import com.example.Test.App.dto.EmployeeDto;
import com.example.Test.App.mapper.EmployeeMapper;
import com.example.Test.App.service.EmployeeService;
import com.example.Test.App.service.KafkaMessage;
import com.example.Test.App.service.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping
public class CheckController {


    private final EmployeeService employeeService;
    private final Producer producer;

    @Autowired
    public CheckController(EmployeeService employeeService, Producer producer) {
        this.employeeService = employeeService;
        this.producer = producer;
    }


    @RequestMapping("/new")
    @PostMapping
    public String create(@RequestBody EmployeeDto employee) {
        Employee empl = EmployeeMapper.MAPPER.mapToEntity(employee);
        if (employeeService.findEmployeeByUsername(employee.getUsername()).isPresent())
            return "A user with this name already exists, please change your Username";
        employeeService.create(empl);
        return "You are registered";

    }

    @RequestMapping("/check")
    @PutMapping
    public String check(
            @RequestParam("username") String username,
            @RequestParam("password") Long password) {
        Optional<Employee> opt = employeeService.findEmployeeByUsername(username);
        if (opt.isPresent()) {
            if (employeeService.isPasswordValid(password, username)) {
                if (opt.get().isActive()) {
                    return "You still working";
                } else {
                    this.producer.sendUser(new KafkaMessage(username, true));
                    return employeeService.startWork(opt.get());
                }
            }
            return "Wrong Password";
        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Employee not found");
    }

    @RequestMapping("/uncheck")
    @PutMapping
    public String uncheck(
            @RequestParam("username") String username,
            @RequestParam("password") Long password) {

        Optional<Employee> opt = employeeService.findEmployeeByUsername(username);
        if (opt.isPresent()) {
            if (employeeService.isPasswordValid(password, username)) {
                if (!(opt.get().isActive())) {
                    return "You are not working now";
                } else {

                    producer.sendUser(new KafkaMessage(username,false));
                    return employeeService.finishWork(opt.get());
                }
            }
            return "Wrong Password";
        } else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Employee not found");

    }

}
