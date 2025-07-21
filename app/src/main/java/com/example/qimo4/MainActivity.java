package com.example.qimo4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qimo4.activity.PatientWelcomeActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.qimo4.model.DatabaseManager;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private RadioGroup roleRadioGroup;
    private CheckBox rememberPasswordCheckBox;

    // SharedPreferences 相关
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_REMEMBER_PASSWORD = "remember_password";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ROLE = "role";

    // 预设的登录凭证
    private static final String PATIENT_USERNAME = "123";
    private static final String PATIENT_PASSWORD = "123";
    private static final String ADMIN_USERNAME = "234";
    private static final String ADMIN_PASSWORD = "234";

    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Log.d(TAG, "onCreate: 开始初始化MainActivity");
            // 初始化 SharedPreferences
            sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

            // 初始化视图
            initViews();

            // 设置点击事件
            setupClickListeners();

            // 恢复保存的登录信息
            restoreLoginInfo();

            // 初始化数据库管理器
            dbManager = new DatabaseManager(this);
            dbManager.open();

            Log.d(TAG, "onCreate: MainActivity初始化完成");

        } catch (Exception e) {
            Log.e(TAG, "onCreate: 初始化失败: " + e.getMessage());
            Toast.makeText(this, "应用初始化失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 关闭数据库连接
        if (dbManager != null) {
            dbManager.close();
        }
    }

    private void initViews() {
        Log.d(TAG, "initViews: 开始初始化视图组件");
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        roleRadioGroup = findViewById(R.id.roleRadioGroup);
        rememberPasswordCheckBox = findViewById(R.id.rememberPasswordCheckBox);
        Log.d(TAG, "initViews: 视图组件初始化完成");
    }

    private void setupClickListeners() {
        Log.d(TAG, "setupClickListeners: 开始设置点击事件");
        if (loginButton != null) {
            loginButton.setOnClickListener(v -> attemptLogin());
        }
        Log.d(TAG, "setupClickListeners: 点击事件设置完成");
    }

    private void restoreLoginInfo() {
        Log.d(TAG, "restoreLoginInfo: 开始恢复登录信息");
        if (sharedPreferences.getBoolean(KEY_REMEMBER_PASSWORD, false)) {
            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");
            boolean isPatient = sharedPreferences.getBoolean(KEY_ROLE, true);

            if (usernameEditText != null) usernameEditText.setText(savedUsername);
            if (passwordEditText != null) passwordEditText.setText(savedPassword);
            if (roleRadioGroup != null) roleRadioGroup.check(isPatient ? R.id.patientRadioButton : R.id.adminRadioButton);
            if (rememberPasswordCheckBox != null) rememberPasswordCheckBox.setChecked(true);
            Log.d(TAG, "restoreLoginInfo: 登录信息恢复完成");
        }
    }

    private void saveLoginInfo(String username, String password, boolean isPatient) {
        Log.d(TAG, "saveLoginInfo: 开始保存登录信息");
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (rememberPasswordCheckBox != null && rememberPasswordCheckBox.isChecked()) {
            editor.putString(KEY_USERNAME, username);
            editor.putString(KEY_PASSWORD, password);
            editor.putBoolean(KEY_ROLE, isPatient);
            editor.putBoolean(KEY_REMEMBER_PASSWORD, true);
        } else {
            editor.clear();
        }
        editor.apply();
        Log.d(TAG, "saveLoginInfo: 登录信息保存完成");
    }

    private void attemptLogin() {
        Log.d(TAG, "attemptLogin: 开始登录验证");
        if (usernameEditText == null || passwordEditText == null || roleRadioGroup == null) {
            Toast.makeText(this, "系统错误，请重试", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "attemptLogin: 视图组件为空");
            return;
        }

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // 输入验证
        if (TextUtils.isEmpty(username)) {
            usernameEditText.setError("请输入账号");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("请输入密码");
            return;
        }

        // 根据选择的角色验证登录
        boolean isPatientSelected = roleRadioGroup.getCheckedRadioButtonId() == R.id.patientRadioButton;

        boolean loginSuccess = false;
        Intent intent = null;

        if (isPatientSelected) {
            // 患者登录验证
            if (username.equals(PATIENT_USERNAME) && password.equals(PATIENT_PASSWORD)) {
                loginSuccess = true;
                intent = new Intent(this, PatientWelcomeActivity.class);
                Log.d(TAG, "attemptLogin: 患者登录验证成功");
            } else {
                Toast.makeText(this, "患者账号或密码错误", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "attemptLogin: 患者登录验证失败");
            }
        } else {
            // 管理员登录验证
            if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
                loginSuccess = true;
                intent = new Intent(this, AdminWelcomeActivity.class);
                Log.d(TAG, "attemptLogin: 管理员登录验证成功");
            } else {
                Toast.makeText(this, "管理员账号或密码错误", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "attemptLogin: 管理员登录验证失败");
            }
        }

        if (loginSuccess && intent != null) {
            // 保存登录信息
            saveLoginInfo(username, password, isPatientSelected);
            // 跳转到对应界面
            startActivity(intent);
            finish();
        }
    }

    // 获取数据库管理器的方法，供其他组件使用
    public DatabaseManager getDbManager() {
        return dbManager;
    }
}