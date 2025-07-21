package com.example.qimo4.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.qimo4.R;
import com.example.qimo4.fragment.PatientHistoryFragment;
import com.example.qimo4.fragment.PatientProfileFragment;
import com.example.qimo4.fragment.PatientTriageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.qimo4.MainActivity;
import com.example.qimo4.model.DatabaseManager;
import com.example.qimo4.model.Department;
import com.example.qimo4.model.TriageRule;
import java.util.List;

public class PatientWelcomeActivity extends AppCompatActivity {
    private static final String TAG = "PatientWelcomeActivity";

    private BottomNavigationView bottomNavigation;
    private DatabaseManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 开始创建Activity");
        setContentView(R.layout.activity_patient_welcome);
        Log.d(TAG, "onCreate: 设置布局完成");

        // 获取数据库管理器
        dbManager = ((MainActivity) getParent()).getDbManager();

        // 测试数据库操作
        testDatabase();

        try {
            initViews();
            setupNavigation();
            
            // 检查FragmentContainer是否存在
            if (findViewById(R.id.patientFragmentContainer) == null) {
                Log.e(TAG, "onCreate: FragmentContainer不存在！");
            } else {
                Log.d(TAG, "onCreate: FragmentContainer存在");
            }

            // 默认显示分诊页面
            if (savedInstanceState == null) {
                Log.d(TAG, "onCreate: 准备添加默认Fragment");
                PatientTriageFragment defaultFragment = new PatientTriageFragment();
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.patientFragmentContainer, defaultFragment)
                    .commit();
                bottomNavigation.setSelectedItemId(R.id.navigation_triage);
                Log.d(TAG, "onCreate: 默认Fragment添加完成");
                
                // 检查Fragment是否已添加
                if (getSupportFragmentManager().findFragmentById(R.id.patientFragmentContainer) != null) {
                    Log.d(TAG, "onCreate: Fragment已成功添加到容器中");
                } else {
                    Log.e(TAG, "onCreate: Fragment添加失败！");
                }
            }

            Log.d(TAG, "onCreate: Activity创建完成");
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Activity创建失败", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity恢复");
        // 检查当前显示的Fragment
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.patientFragmentContainer);
        if (currentFragment != null) {
            Log.d(TAG, "onResume: 当前显示的Fragment: " + currentFragment.getClass().getSimpleName());
        } else {
            Log.e(TAG, "onResume: 没有Fragment在显示！");
        }
    }

    private void initViews() {
        Log.d(TAG, "initViews: 开始初始化视图");
        try {
            bottomNavigation = findViewById(R.id.bottomNavigation);
            if (bottomNavigation == null) {
                Log.e(TAG, "initViews: 底部导航栏未找到！");
            } else {
                Log.d(TAG, "initViews: 底部导航栏初始化成功");
            }
        } catch (Exception e) {
            Log.e(TAG, "initViews: 视图初始化失败", e);
            throw e;
        }
    }

    private void setupNavigation() {
        Log.d(TAG, "setupNavigation: 开始设置导航");
        try {
            bottomNavigation.setOnItemSelectedListener(item -> {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();
                Log.d(TAG, "setupNavigation: 选择的菜单项ID: " + itemId);
                
                if (itemId == R.id.navigation_triage) {
                    selectedFragment = new PatientTriageFragment();
                    Log.d(TAG, "setupNavigation: 创建分诊Fragment");
                } else if (itemId == R.id.navigation_history) {
                    selectedFragment = new PatientHistoryFragment();
                    Log.d(TAG, "setupNavigation: 创建病史Fragment");
                } else if (itemId == R.id.navigation_profile) {
                    selectedFragment = new PatientProfileFragment();
                    Log.d(TAG, "setupNavigation: 创建个人中心Fragment");
                }

                if (selectedFragment != null) {
                    Log.d(TAG, "setupNavigation: 开始替换Fragment: " + selectedFragment.getClass().getSimpleName());
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.patientFragmentContainer, selectedFragment)
                        .commit();
                    Log.d(TAG, "setupNavigation: Fragment替换完成");
                    return true;
                }
                Log.e(TAG, "setupNavigation: 未能创建Fragment");
                return false;
            });
            Log.d(TAG, "setupNavigation: 导航设置完成");
        } catch (Exception e) {
            Log.e(TAG, "setupNavigation: 导航设置失败", e);
            throw e;
        }
    }

    private void testDatabase() {
        try {
            // 添加测试科室
            Department department = new Department();
            department.setName("测试科室");
            department.setDescription("这是一个测试科室");
            long deptId = dbManager.addDepartment(department);
            Log.d(TAG, "添加科室成功，ID: " + deptId);

            // 查询所有科室
            List<Department> departments = dbManager.getAllDepartments();
            for (Department dept : departments) {
                Log.d(TAG, "科室: " + dept.getName() + ", 描述: " + dept.getDescription());
            }

            // 添加测试分诊规则
            TriageRule rule = new TriageRule();
            rule.setName("测试规则");
            rule.setDescription("这是一个测试分诊规则");
            rule.setPriority(1);
            long ruleId = dbManager.addTriageRule(rule);
            Log.d(TAG, "添加分诊规则成功，ID: " + ruleId);

            // 查询所有分诊规则
            List<TriageRule> rules = dbManager.getAllTriageRules();
            for (TriageRule r : rules) {
                Log.d(TAG, "规则: " + r.getName() + ", 优先级: " + r.getPriority());
            }

        } catch (Exception e) {
            Log.e(TAG, "数据库测试失败", e);
        }
    }
} 