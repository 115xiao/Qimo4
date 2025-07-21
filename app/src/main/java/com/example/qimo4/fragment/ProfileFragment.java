package com.example.qimo4.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qimo4.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

    private ShapeableImageView avatarImage;
    private TextView nameText;
    private TextView roleText;
    private TextView departmentText;
    private MaterialButton editProfileButton;
    private TextView todayTriageCount;
    private TextView activeRulesCount;
    private TextView accuracyRate;
    private MaterialButton systemManageButton;
    private MaterialButton userManageButton;
    private MaterialButton dataAnalysisButton;
    private MaterialButton logViewButton;
    private MaterialButton settingsButton;
    private MaterialButton logoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 开始创建Fragment视图");
        try {
            View view = inflater.inflate(R.layout.fragment_profile, container, false);
            Log.d(TAG, "onCreateView: 布局加载成功");

            initViews(view);
            setupListeners();
            loadUserInfo();
            loadStatistics();

            Log.d(TAG, "onCreateView: Fragment初始化完成");
            return view;
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Fragment创建失败", e);
            throw e;
        }
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: 开始初始化视图组件");
        try {
            avatarImage = view.findViewById(R.id.avatarImage);
            nameText = view.findViewById(R.id.nameText);
            roleText = view.findViewById(R.id.roleText);
            departmentText = view.findViewById(R.id.departmentText);
            editProfileButton = view.findViewById(R.id.editProfileButton);
            todayTriageCount = view.findViewById(R.id.todayTriageCount);
            activeRulesCount = view.findViewById(R.id.activeRulesCount);
            accuracyRate = view.findViewById(R.id.accuracyRate);
            systemManageButton = view.findViewById(R.id.systemManageButton);
            userManageButton = view.findViewById(R.id.userManageButton);
            dataAnalysisButton = view.findViewById(R.id.dataAnalysisButton);
            logViewButton = view.findViewById(R.id.logViewButton);
            settingsButton = view.findViewById(R.id.settingsButton);
            logoutButton = view.findViewById(R.id.logoutButton);

            Log.d(TAG, "initViews: 视图组件初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "initViews: 视图初始化失败", e);
            throw e;
        }
    }

    private void setupListeners() {
        Log.d(TAG, "setupListeners: 开始设置监听器");
        try {
            // 编辑个人信息
            editProfileButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), "编辑个人信息功能开发中", Toast.LENGTH_SHORT).show();
            });

            // 系统管理
            systemManageButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), "系统管理功能开发中", Toast.LENGTH_SHORT).show();
            });

            // 用户管理
            userManageButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), "用户管理功能开发中", Toast.LENGTH_SHORT).show();
            });

            // 数据分析
            dataAnalysisButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), "数据分析功能开发中", Toast.LENGTH_SHORT).show();
            });

            // 日志查看
            logViewButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), "日志查看功能开发中", Toast.LENGTH_SHORT).show();
            });

            // 系统设置
            settingsButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), "系统设置功能开发中", Toast.LENGTH_SHORT).show();
            });

            // 退出登录
            logoutButton.setOnClickListener(v -> {
                showLogoutConfirmDialog();
            });

            Log.d(TAG, "setupListeners: 监听器设置完成");
        } catch (Exception e) {
            Log.e(TAG, "setupListeners: 监听器设置失败", e);
            throw e;
        }
    }

    private void loadUserInfo() {
        Log.d(TAG, "loadUserInfo: 开始加载用户信息");
        try {
            // TODO: 从服务器或本地数据库加载用户信息
            nameText.setText("张三");
            roleText.setText("系统管理员");
            departmentText.setText("信息科");
            Log.d(TAG, "loadUserInfo: 用户信息加载完成");
        } catch (Exception e) {
            Log.e(TAG, "loadUserInfo: 用户信息加载失败", e);
            throw e;
        }
    }

    private void loadStatistics() {
        Log.d(TAG, "loadStatistics: 开始加载统计数据");
        try {
            // TODO: 从服务器加载实时统计数据
            todayTriageCount.setText("128");
            activeRulesCount.setText("45");
            accuracyRate.setText("95%");
            Log.d(TAG, "loadStatistics: 统计数据加载完成");
        } catch (Exception e) {
            Log.e(TAG, "loadStatistics: 统计数据加载失败", e);
            throw e;
        }
    }

    private void showLogoutConfirmDialog() {
        Log.d(TAG, "showLogoutConfirmDialog: 显示退出确认对话框");
        try {
            new MaterialAlertDialogBuilder(requireContext())
                .setTitle("退出登录")
                .setMessage("确定要退出登录吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    // TODO: 执行退出登录操作
                    Toast.makeText(getContext(), "退出登录成功", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("取消", null)
                .show();
            Log.d(TAG, "showLogoutConfirmDialog: 对话框显示成功");
        } catch (Exception e) {
            Log.e(TAG, "showLogoutConfirmDialog: 对话框显示失败", e);
            throw e;
        }
    }
}
