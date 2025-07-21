package com.example.qimo4.model;

public class TriageRule {
    private String symptoms;          // 症状描述
    private String vitalSigns;        // 生命体征
    private int triageLevel;         // 分诊等级（1-5）
    private String targetDepartment;  // 目标科室
    private int priority;            // 优先级（1-10）
    private boolean isActive;        // 规则是否启用

    public TriageRule(String symptoms, String vitalSigns) {
        this.symptoms = symptoms;
        this.vitalSigns = vitalSigns;
        this.triageLevel = 3;  // 默认为3级
        this.priority = 5;     // 默认优先级为5
        this.isActive = true;  // 默认启用
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(String vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public int getTriageLevel() {
        return triageLevel;
    }

    public void setTriageLevel(int triageLevel) {
        if (triageLevel >= 1 && triageLevel <= 5) {
            this.triageLevel = triageLevel;
        }
    }

    public String getTargetDepartment() {
        return targetDepartment;
    }

    public void setTargetDepartment(String targetDepartment) {
        this.targetDepartment = targetDepartment;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        if (priority >= 1 && priority <= 10) {
            this.priority = priority;
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getTriageLevelDescription() {
        switch (triageLevel) {
            case 1:
                return "一级（危重）";
            case 2:
                return "二级（急症）";
            case 3:
                return "三级（亚急症）";
            case 4:
                return "四级（普通）";
            default:
                return "待定";
        }
    }
} 