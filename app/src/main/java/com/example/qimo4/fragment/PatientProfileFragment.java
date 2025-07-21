package com.example.qimo4.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qimo4.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

public class PatientProfileFragment extends Fragment {
    private static final String TAG = "PatientProfileFragment";

    private TextView nameText;
    private TextView idCardText;
    private TextView phoneText;
    private MaterialButton editProfileButton;
    private MaterialButton logoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 开始创建Fragment视图");
        try {
            View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);
            
            initViews(view);
            setupListeners();
            loadUserInfo();

            Log.d(TAG, "onCreateView: Fragment创建完成");
            return view;
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Fragment创建失败", e);
            throw e;
        }
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: 开始初始化视图组件");
        try {
            nameText = view.findViewById(R.id.nameText);
            idCardText = view.findViewById(R.id.idCardText);
            phoneText = view.findViewById(R.id.phoneText);
            editProfileButton = view.findViewById(R.id.editProfileButton);
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
            editProfileButton.setOnClickListener(v -> {
                if (editProfileButton.getText().toString().equals("编辑")) {
                    // 进入编辑模式
                    showEditDialog();
                } else {
                    // 保存修改
                    saveUserInfo();
                }
            });

            logoutButton.setOnClickListener(v -> showLogoutConfirmDialog());

            Log.d(TAG, "setupListeners: 监听器设置完成");
        } catch (Exception e) {
            Log.e(TAG, "setupListeners: 监听器设置失败", e);
            throw e;
        }
    }

    private void showEditDialog() {
        Log.d(TAG, "showEditDialog: 显示编辑对话框");
        try {
            // 创建对话框的布局
            View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_profile, null);
            
            // 获取输入框
            TextInputEditText nameInput = dialogView.findViewById(R.id.nameInput);
            TextInputEditText idCardInput = dialogView.findViewById(R.id.idCardInput);
            TextInputEditText phoneInput = dialogView.findViewById(R.id.phoneInput);
            
            // 设置当前值
            nameInput.setText(nameText.getText());
            idCardInput.setText(idCardText.getText());
            phoneInput.setText(phoneText.getText());

            // 显示对话框
            new MaterialAlertDialogBuilder(requireContext())
                .setTitle("编辑个人信息")
                .setView(dialogView)
                .setPositiveButton("保存", (dialog, which) -> {
                    // 保存修改的信息
                    String newName = nameInput.getText().toString();
                    String newIdCard = idCardInput.getText().toString();
                    String newPhone = phoneInput.getText().toString();

                    // 验证输入
                    if (validateInput(newName, newIdCard, newPhone)) {
                        // 更新显示
                        nameText.setText(newName);
                        idCardText.setText(newIdCard);
                        phoneText.setText(newPhone);
                        
                        // 保存到本地或服务器
                        saveUserInfo();
                    }
                })
                .setNegativeButton("取消", null)
                .show();

            Log.d(TAG, "showEditDialog: 编辑对话框显示成功");
        } catch (Exception e) {
            Log.e(TAG, "showEditDialog: 显示编辑对话框失败", e);
            Toast.makeText(getContext(), "显示编辑对话框失败", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String name, String idCard, String phone) {
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (idCard.isEmpty() || idCard.length() != 18) {
            Toast.makeText(getContext(), "请输入18位身份证号", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.isEmpty() || phone.length() != 11) {
            Toast.makeText(getContext(), "请输入11位手机号", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void loadUserInfo() {
        Log.d(TAG, "loadUserInfo: 开始加载用户信息");
        try {
            // 从SharedPreferences加载数据
            SharedPreferences prefs = requireContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String name = prefs.getString("name", "张三");
            String idCard = prefs.getString("idCard", "110101199001011234");
            String phone = prefs.getString("phone", "13800138000");

            nameText.setText(name);
            idCardText.setText(idCard);
            phoneText.setText(phone);
            
            Log.d(TAG, "loadUserInfo: 用户信息加载完成");
        } catch (Exception e) {
            Log.e(TAG, "loadUserInfo: 用户信息加载失败", e);
            throw e;
        }
    }

    private void saveUserInfo() {
        Log.d(TAG, "saveUserInfo: 开始保存用户信息");
        try {
            // 保存到SharedPreferences
            SharedPreferences prefs = requireContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", nameText.getText().toString());
            editor.putString("idCard", idCardText.getText().toString());
            editor.putString("phone", phoneText.getText().toString());
            editor.apply();

            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "saveUserInfo: 用户信息保存完成");
        } catch (Exception e) {
            Log.e(TAG, "saveUserInfo: 用户信息保存失败", e);
            Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void setInputEnabled(boolean enabled) {
        // 由于使用TextView，编辑功能将通过对话框实现
    }

    private void showLogoutConfirmDialog() {
        new MaterialAlertDialogBuilder(requireContext())
            .setTitle("退出登录")
            .setMessage("确定要退出登录吗？")
            .setPositiveButton("确定", (dialog, which) -> {
                // TODO: 执行退出登录操作
                requireActivity().finish();
            })
            .setNegativeButton("取消", null)
            .show();
    }
} 