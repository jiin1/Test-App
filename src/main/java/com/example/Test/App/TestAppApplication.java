package com.example.Test.App;

import com.example.Test.App.Controller.CheckController;
import com.example.Test.App.dao.Employee;
import com.example.Test.App.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.logging.Logger;

/**
 * @author Andrew Yantsen
 */

@SpringBootApplication
@EnableSwagger2
public class TestAppApplication implements CommandLineRunner {
    @Autowired
    EmployeeRepo employeeRepo;
    public static final Logger logger = Logger.getLogger(TestAppApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(TestAppApplication.class, args);
    }

    public void run(String... args) throws Exception {
        logger.info("Создаем в БД 100 тестовых пользователей");
        for (int i = 0; i <= 100; i++)
            employeeRepo.save(new Employee(String.valueOf(i), (long) 1, "Boris" + i, "TheBlade" + i));

    }
}
