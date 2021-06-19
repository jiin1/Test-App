package com.example.Test.App.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@ToString(of = {"name","surname","creationDate"})
@EqualsAndHashCode(of= {"id"})
public class Employee {
    public Employee(String username, Long password, String name, String surname) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private Long password;
    private String name;
    private String surname;
    private boolean isActive;

    @Column(updatable = false)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL )
    @JoinColumn(name = "employee_id")
    private List<WorkingTime> workingTimeList = new ArrayList<>();

    public List<WorkingTime> getWorkingTimeList() {
        return workingTimeList;
    }

    public void setWorkingTimeList(List<WorkingTime> workingTimeList) {
        this.workingTimeList = workingTimeList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getPassword() {
        return password;
    }

    public void setPassword(Long password) {
        this.password = password;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

}

