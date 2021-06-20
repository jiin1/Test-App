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
import java.util.logging.Logger;

/**
 * @author Andrew Yantsen
 */

@RestController
@RequestMapping("/api")
public class CheckController {

    public static final Logger logger = Logger.getLogger(CheckController.class.getName());


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
        logger.info("Ищем " + employee.getUsername() + " в существующих пользователях");
        Employee empl = EmployeeMapper.MAPPER.mapToEntity(employee);
        if (employeeService.findEmployeeByUsername(employee.getUsername()).isPresent()) {
            logger.info("Такой пользователь уже существует можно начинать работу");
            return "A user with this name already exists, please change your Username";
        }
        logger.info("Создаем нового пользователя");
        employeeService.create(empl);
        return "You are registered";

    }

    @RequestMapping("/check")
    @PutMapping
    public String check(
            @RequestParam("username") String username,
            @RequestParam("password") Long password) {
        logger.info("Ищем " + username + " среди существующих пользователей");
        Optional<Employee> opt = employeeService.findEmployeeByUsername(username);
        if (opt.isPresent()) {
            if (employeeService.isPasswordValid(password, username)) {
                if (opt.get().isActive()) {
                    logger.info("Пользователь уже работает");
                    return "You still working";
                } else {
                    logger.info("Регистрируем время начала работы, отправляем сообщение сервису №2");
                    this.producer.sendUser(new KafkaMessage(username, true));
                    return employeeService.startWork(opt.get());
                }
            }
            logger.info("Неправильный пароль");
            return "Wrong Password";
        } else {
            logger.info("Такого пользователя нет в БД");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

    @RequestMapping("/uncheck")
    @PutMapping
    public String uncheck(
            @RequestParam("username") String username,
            @RequestParam("password") Long password) {
        logger.info("Ищем " + username + " среди существующих пользователей");
        Optional<Employee> opt = employeeService.findEmployeeByUsername(username);
        if (opt.isPresent()) {
            if (employeeService.isPasswordValid(password, username)) {
                if (!(opt.get().isActive())) {
                    logger.info("Пользователь уже  не работает");
                    return "You are not working now";
                } else {
                    logger.info("Регистрируем время окончания работы, отправляем сообщение сервису №2");
                    producer.sendUser(new KafkaMessage(username, false));
                    return employeeService.finishWork(opt.get());
                }
            }
            logger.info("Неправильный пароль");
            return "Wrong Password";
        } else {
            logger.info("Такого пользователя нет в БД");
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

}

