package com.example.Test.App.service;

import com.example.Test.App.dao.Employee;
import com.example.Test.App.dao.WorkingTime;
import com.example.Test.App.repo.EmployeeRepo;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Andrew Yantsen
 */

@Service
public class Consumer {
    private final EmployeeRepo employeeRepo;
    private final Timer timer = new Timer();
    private Double salary = 0.0;

    public Consumer(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    @KafkaListener(topics = "first_topic", groupId = "group")
    public void consumeUser(KafkaMessage kafkaMessage){
        // получили username и isActive

        Optional<Employee> opti = employeeRepo.findEmployeeByUsername(kafkaMessage.getUserName());
        if(opti.isPresent()){
            Employee employee = opti.get();
            WorkingTime wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
                if(kafkaMessage.getActive()){
                 wor.setSalary((double) 0);

                  timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        salary += 1.0 / 6.0;
                    }
                }, 1000, 1000);

                  timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        WorkingTime wor = employee.getWorkingTimeList().get(employee.getWorkingTimeList().size() - 1);
                        wor.setSalary(salary);
                        employeeRepo.saveAndFlush(employee);
                       }
                }, 0, 10 * 1000);

              }else{
                wor.setSalary(salary);
                employeeRepo.saveAndFlush(employee);
                timer.cancel();
                }
        }
    //    System.out.println("получил юзера: "+kafkaMessage.toString());


    }
}
