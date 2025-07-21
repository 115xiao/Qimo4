package com.example.qimo4;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminWelcomeActivity extends AppCompatActivity {

    private static final String TAG = "AdminWelcomeActivity";
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: 开始创建AdminWelcomeActivity");
        
        try {
            setContentView(R.layout.activity_admin_welcome);
            Log.d(TAG, "onCreate: 布局设置成功");

            // 设置Toolbar
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Log.d(TAG, "onCreate: Toolbar设置成功");

            // 设置底部导航
            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            if (bottomNav == null) {
                Log.e(TAG, "onCreate: 底部导航栏未找到");
                return;
            }
            Log.d(TAG, "onCreate: 底部导航栏找到");
            
            // 使用NavHostFragment获取NavController
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
            
            if (navHostFragment == null) {
                Log.e(TAG, "onCreate: NavHostFragment未找到");
                return;
            }
            
            navController = navHostFragment.getNavController();
            Log.d(TAG, "onCreate: Navigation Controller 初始化成功");

            // 配置底部导航
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_department,
                R.id.navigation_triage,
                R.id.navigation_statistics,
                R.id.navigation_profile
            ).build();
            Log.d(TAG, "onCreate: AppBarConfiguration 配置完成");

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(bottomNav, navController);
            Log.d(TAG, "onCreate: Navigation设置完成");

            // 添加导航监听器
            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                Log.d(TAG, "导航到: " + destination.getLabel() + ", id: " + destination.getId());
            });

            // 设置菜单项点击监听器
            bottomNav.setOnItemSelectedListener(item -> {
                Log.d(TAG, "菜单项被点击: " + item.getTitle() + ", id: " + item.getItemId());
                return NavigationUI.onNavDestinationSelected(item, navController);
            });
        } catch (Exception e) {
            Log.e(TAG, "onCreate: 发生异常", e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Activity开始");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Activity准备交互");
    }

    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }
} 