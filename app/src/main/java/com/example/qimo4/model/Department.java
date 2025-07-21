package com.example.qimo4.model;

import java.util.Date;

public class Department {
    private String id;
    private String name;
    private String description;
    private String schedule;
    private int fee;
    private boolean isEnabled;
    private Date lastUpdateTime;
    private String status;
    private String doctor;
    private String expertise;
    private int waitingTime;
    private String location;

    public Department() {
        this.isEnabled = true;
        this.lastUpdateTime = new Date();
    }

    public Department(String name, String description, String schedule, int fee,
                     String status, String doctor, String expertise, int waitingTime, String location) {
        this();  // 调用默认构造函数初始化基本字段
        this.name = name;
        this.description = description;
        this.schedule = schedule;
        this.fee = fee;
        this.status = status;
        this.doctor = doctor;
        this.expertise = expertise;
        this.waitingTime = waitingTime;
        this.location = location;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public int getFee() { return fee; }
    public void setFee(int fee) { this.fee = fee; }

    public boolean isEnabled() { return isEnabled; }
    public void setEnabled(boolean enabled) { 
        this.isEnabled = enabled;
        this.lastUpdateTime = new Date();
    }

    public Date getLastUpdateTime() { return lastUpdateTime; }
    public void setLastUpdateTime(Date lastUpdateTime) { this.lastUpdateTime = lastUpdateTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDoctor() { return doctor; }
    public void setDoctor(String doctor) { this.doctor = doctor; }

    public String getExpertise() { return expertise; }
    public void setExpertise(String expertise) { this.expertise = expertise; }

    public int getWaitingTime() { return waitingTime; }
    public void setWaitingTime(int waitingTime) { this.waitingTime = waitingTime; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
} 