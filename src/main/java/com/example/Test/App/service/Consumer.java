package com.example.Test.App.service;

import com.example.Test.App.dao.Employee;
import com.example.Test.App.dao.WorkingTime;
import com.example.Test.App.repo.EmployeeRepo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Andrew Yantsen
 */

@Service
public class Consumer {
    private final EmployeeRepo employeeRepo;
    private final Map<String, List<ScheduledFuture<?>>> timerMap = new HashMap<String, List<ScheduledFuture<?>>>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(12);

    public Consumer(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @KafkaListener(topics = "first_topic", groupId = "group")
    public void consumeUser(KafkaMessage kafkaMessage) {
        AtomicReference<Long> salary = new AtomicReference<>(0L);
        Optional<Employee> optional = employeeRepo.findEmployeeByUsername(kafkaMessage.getUserName());
        if (optional.isPresent()) {
            Employee employee = optional.get();
            WorkingTime wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
            if (kafkaMessage.getActive()) {
                wor.setSalary(salary.get());

                Runnable runnable = () -> {
                    NumberFormat formatter = new DecimalFormat();
                    salary.updateAndGet(v -> v + 1000);
                    System.out.println("показания счетчика: " + formatter.format(salary.get()));
                };
                final ScheduledFuture<?> futureTimer = scheduler.scheduleAtFixedRate(runnable, 60, 60, SECONDS);
                final ScheduledFuture<?> futureTimerRepo = scheduler.scheduleAtFixedRate(() -> {
                    wor.setSalary(salary.get());
                    employeeRepo.saveAndFlush(employee);
                    System.out.println("записываем в базу зп: " + salary.get());
                }, 0, 60 * 60, SECONDS);
                List<ScheduledFuture<?>> listFuture = new ArrayList<>();
                listFuture.add(futureTimer);
                listFuture.add(futureTimerRepo);

                timerMap.put(kafkaMessage.getUserName(), listFuture);


            } else {
                List<ScheduledFuture<?>> listFuture = timerMap.get(kafkaMessage.getUserName());
                for (ScheduledFuture<?> future : listFuture) {
                    future.cancel(true);
                }

            }
        }
    }
}