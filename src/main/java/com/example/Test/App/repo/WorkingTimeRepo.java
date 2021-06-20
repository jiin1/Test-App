package com.example.Test.App.repo;

import com.example.Test.App.dao.Employee;
import com.example.Test.App.dao.WorkingTime;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Andrew Yantsen
 */

public interface WorkingTimeRepo  extends JpaRepository<WorkingTime,Long> {
}
