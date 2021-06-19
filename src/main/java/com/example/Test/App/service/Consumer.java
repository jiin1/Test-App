package com.example.Test.App.service;

import com.example.Test.App.dao.Employee;
import com.example.Test.App.dao.WorkingTime;
import com.example.Test.App.repo.EmployeeRepo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Andrew Yantsen
 */

@Service
public class Consumer {
    private final EmployeeRepo employeeRepo;
    private final Map<String, EmployeeHolder> timerMap = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(12);

    public Consumer(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @KafkaListener(topics = "first_topic", groupId = "group")
    public void consumeUser(KafkaMessage kafkaMessage) {
        AtomicLong salary = new AtomicLong(0L);
        Optional<Employee> optional = employeeRepo.findEmployeeByUsername(kafkaMessage.getUserName());
        if (optional.isPresent()) {
            Employee employee = optional.get();
            WorkingTime wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
            if (kafkaMessage.getActive()) {
                wor.setSalary(salary.get());
                EmployeeHolder employeeHolder = new EmployeeHolder(employee);
                timerMap.put(kafkaMessage.getUserName(), employeeHolder);


            } else {
                EmployeeHolder employeeHolder = timerMap.get(kafkaMessage.getUserName());
                employeeHolder.scheduledFuture1.cancel(true);
                employeeHolder.scheduledFuture2.cancel(true);
                Employee employeeRepo1 = employeeRepo.findEmployeeByUsername(kafkaMessage.getUserName()).get();
                employeeRepo1.getWorkingTimeList().get(employeeRepo1.getWorkingTimeList().size() - 1).setSalary(employeeHolder.currentSalary.get());
                employeeRepo.saveAndFlush(employeeRepo1);
            }
        }
    }

    private class EmployeeHolder {
        Employee employee;
        AtomicLong currentSalary;
        Runnable runnable;
        WorkingTime wor;
        ScheduledFuture<?> scheduledFuture1;
        ScheduledFuture<?> scheduledFuture2;

        public EmployeeHolder(Employee employee) {
            this.employee = employee;
            currentSalary = new AtomicLong(0);
            wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
            runnable = () -> {
                currentSalary.updateAndGet(v -> v + 1000);
            };
            scheduledFuture1 = scheduler.scheduleAtFixedRate(runnable, 60, 60, SECONDS);
            scheduledFuture2 = scheduler.scheduleAtFixedRate(() -> {
                wor.setSalary(currentSalary.get());
                employeeRepo.saveAndFlush(employee);
            }, 0, 60*60, SECONDS);
        }
    }
}