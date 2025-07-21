package com.example.qimo4.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // 科室相关操作
    public long addDepartment(Department department) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_DEPT_NAME, department.getName());
        values.put(DatabaseHelper.COLUMN_DEPT_DESCRIPTION, department.getDescription());
        
        return database.insert(DatabaseHelper.TABLE_DEPARTMENTS, null, values);
    }

    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_DEPARTMENTS,
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Department department = new Department();
                department.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                department.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DEPT_NAME)));
                department.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DEPT_DESCRIPTION)));
                departments.add(department);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return departments;
    }

    // 分诊规则相关操作
    public long addTriageRule(TriageRule rule) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_RULE_NAME, rule.getName());
        values.put(DatabaseHelper.COLUMN_RULE_DESCRIPTION, rule.getDescription());
        values.put(DatabaseHelper.COLUMN_PRIORITY, rule.getPriority());
        
        return database.insert(DatabaseHelper.TABLE_TRIAGE_RULES, null, values);
    }

    public List<TriageRule> getAllTriageRules() {
        List<TriageRule> rules = new ArrayList<>();
        Cursor cursor = database.query(DatabaseHelper.TABLE_TRIAGE_RULES,
                null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TriageRule rule = new TriageRule();
                rule.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                rule.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RULE_NAME)));
                rule.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_RULE_DESCRIPTION)));
                rule.setPriority(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_PRIORITY)));
                rules.add(rule);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return rules;
    }

    // 就医历史相关操作
    public long addMedicalHistory(MedicalHistory history) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PATIENT_ID, history.getPatientId());
        values.put(DatabaseHelper.COLUMN_DIAGNOSIS, history.getDiagnosis());
        values.put(DatabaseHelper.COLUMN_TREATMENT, history.getTreatment());
        values.put(DatabaseHelper.COLUMN_VISIT_DATE, history.getVisitDate());
        
        return database.insert(DatabaseHelper.TABLE_MEDICAL_HISTORY, null, values);
    }

    public List<MedicalHistory> getMedicalHistoryByPatientId(long patientId) {
        List<MedicalHistory> histories = new ArrayList<>();
        String selection = DatabaseHelper.COLUMN_PATIENT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(patientId)};
        
        Cursor cursor = database.query(DatabaseHelper.TABLE_MEDICAL_HISTORY,
                null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                MedicalHistory history = new MedicalHistory();
                history.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                history.setPatientId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_PATIENT_ID)));
                history.setDiagnosis(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DIAGNOSIS)));
                history.setTreatment(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TREATMENT)));
                history.setVisitDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_VISIT_DATE)));
                histories.add(history);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return histories;
    }
} 