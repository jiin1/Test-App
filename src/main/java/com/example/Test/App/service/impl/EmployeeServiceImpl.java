package com.example.Test.App.service.impl;

import com.example.Test.App.Controller.CheckController;
import com.example.Test.App.dao.Employee;
import com.example.Test.App.dao.WorkingTime;
import com.example.Test.App.repo.EmployeeRepo;
import com.example.Test.App.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * @author Andrew Yantsen
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;
    public static final Logger logger = Logger.getLogger(EmployeeServiceImpl.class.getName());


    public EmployeeServiceImpl(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;

    }


    @Override
    public Optional<Employee> findEmployeeByUsername(String username) {
        logger.info("Ищем пользователя в БД");
        return employeeRepo.findEmployeeByUsername(username);
    }


    @Override
    public Boolean isPasswordValid(Long password, String username) {
        logger.info("Проверяем совпадение пароля и логина");
        Optional<Employee> opti = employeeRepo.findEmployeeByUsername(username);
        if (opti.isPresent()) {
            return opti.get().getPassword().equals(password);
        } else return null;
    }

    @Override
    public String startWork(Employee employee) {
        logger.info("Добавляем к работнику новую строку времени работы");
        employee.getWorkingTimeList().add(new WorkingTime());
        logger.info("Находим последнюю строку в списке работ");
        WorkingTime wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
        logger.info("Устанавливаем время начала работы");
        wor.setStartTime(LocalDateTime.now());
        logger.info("Устанавливаем активность работника - true");
        employee.setActive(true);
        logger.info("Сохраняем изменения");
        employeeRepo.save(employee);
        return "Your work is started on " + employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1).getStartTime();
    }

    @Override
    public String finishWork(Employee employee) {
        logger.info("Устанавливаем активность работника - false");
        employee.setActive(false);
        logger.info("Находим последнюю строку в списке работ");
        WorkingTime wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
        logger.info("Устанавливаем время окончания работы");
        wor.setFinishTime(LocalDateTime.now());
        logger.info("Сохраняем изменения");
        employeeRepo.saveAndFlush(employee);
        return "Your work is finished on " + employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1).getFinishTime();
    }

    @Override
    public void create(Employee employee) {
        logger.info("Создаем нового работника и указываем время его создания");
        employee.setCreationDate(LocalDateTime.now());
        logger.info("Устанавливаем активность работника - false");
        employee.setActive(false);
        employeeRepo.save(employee);
    }
}
