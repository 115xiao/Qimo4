package com.example.qimo4.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hospital.db";
    private static final int DATABASE_VERSION = 1;

    // 表名
    public static final String TABLE_DEPARTMENTS = "departments";
    public static final String TABLE_MEDICAL_HISTORY = "medical_history";
    public static final String TABLE_TRIAGE_RULES = "triage_rules";
    public static final String TABLE_PATIENTS = "patients";

    // 通用列
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CREATED_AT = "created_at";

    // Departments表列
    public static final String COLUMN_DEPT_NAME = "name";
    public static final String COLUMN_DEPT_DESCRIPTION = "description";

    // Medical History表列
    public static final String COLUMN_PATIENT_ID = "patient_id";
    public static final String COLUMN_DIAGNOSIS = "diagnosis";
    public static final String COLUMN_TREATMENT = "treatment";
    public static final String COLUMN_VISIT_DATE = "visit_date";

    // Triage Rules表列
    public static final String COLUMN_RULE_NAME = "name";
    public static final String COLUMN_RULE_DESCRIPTION = "description";
    public static final String COLUMN_PRIORITY = "priority";

    // Patients表列
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_PHONE = "phone";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建科室表
        String CREATE_DEPARTMENTS_TABLE = "CREATE TABLE " + TABLE_DEPARTMENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DEPT_NAME + " TEXT NOT NULL,"
                + COLUMN_DEPT_DESCRIPTION + " TEXT,"
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        // 创建患者表
        String CREATE_PATIENTS_TABLE = "CREATE TABLE " + TABLE_PATIENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT NOT NULL,"
                + COLUMN_AGE + " INTEGER,"
                + COLUMN_GENDER + " TEXT,"
                + COLUMN_PHONE + " TEXT,"
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        // 创建就医历史表
        String CREATE_MEDICAL_HISTORY_TABLE = "CREATE TABLE " + TABLE_MEDICAL_HISTORY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PATIENT_ID + " INTEGER,"
                + COLUMN_DIAGNOSIS + " TEXT,"
                + COLUMN_TREATMENT + " TEXT,"
                + COLUMN_VISIT_DATE + " DATETIME,"
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + "FOREIGN KEY(" + COLUMN_PATIENT_ID + ") REFERENCES " + TABLE_PATIENTS + "(" + COLUMN_ID + ")"
                + ")";

        // 创建分诊规则表
        String CREATE_TRIAGE_RULES_TABLE = "CREATE TABLE " + TABLE_TRIAGE_RULES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RULE_NAME + " TEXT NOT NULL,"
                + COLUMN_RULE_DESCRIPTION + " TEXT,"
                + COLUMN_PRIORITY + " INTEGER,"
                + COLUMN_CREATED_AT + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";

        db.execSQL(CREATE_DEPARTMENTS_TABLE);
        db.execSQL(CREATE_PATIENTS_TABLE);
        db.execSQL(CREATE_MEDICAL_HISTORY_TABLE);
        db.execSQL(CREATE_TRIAGE_RULES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 在数据库升级时删除旧表
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICAL_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRIAGE_RULES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEPARTMENTS);
        
        // 重新创建表
        onCreate(db);
    }
} 