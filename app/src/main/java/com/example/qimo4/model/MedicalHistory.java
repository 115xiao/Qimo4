package com.example.qimo4.model;

public class MedicalHistory {
    private String visitDate;        // 就诊日期
    private String symptoms;         // 症状描述
    private String diagnosis;        // 诊断结果
    private String medicalHistory;   // 既往病史

    public MedicalHistory(String visitDate, String symptoms, String diagnosis, String medicalHistory) {
        this.visitDate = visitDate;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.medicalHistory = medicalHistory;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
} 