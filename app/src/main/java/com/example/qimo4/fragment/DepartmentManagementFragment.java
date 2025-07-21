package com.example.qimo4.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.qimo4.R;
import com.example.qimo4.adapter.DepartmentAdapter;
import com.example.qimo4.model.Department;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class DepartmentManagementFragment extends Fragment implements DepartmentAdapter.OnDepartmentClickListener {
    private RecyclerView recyclerView;
    private DepartmentAdapter adapter;
    private TextView emptyView;
    private FloatingActionButton addButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_department_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化视图
            recyclerView = view.findViewById(R.id.departmentRecyclerView);
            emptyView = view.findViewById(R.id.emptyView);
        addButton = view.findViewById(R.id.addDepartmentFab);
        
        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DepartmentAdapter(this);
        recyclerView.setAdapter(adapter);

        // 设置添加按钮点击事件
        addButton.setOnClickListener(v -> showAddDepartmentDialog());
        
        // 加载科室列表
        loadDepartments();
    }

    private void loadDepartments() {
        // 这里应该从服务器或本地数据库加载数据
        // 现在使用模拟数据
        List<Department> departments = getMockDepartments();
        
        if (departments.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.setDepartments(departments);
        }
    }

    private List<Department> getMockDepartments() {
        List<Department> departments = new ArrayList<>();
        
        // 添加一些模拟数据
        departments.add(new Department(
            "内科",
            "专治内科常见病、多发病",
            "周一至周五 8:00-17:00",
            30,
            "正常",
            "张医生--主任医师",
            "内科常见病、慢性病管理",
            15,
            "门诊楼2层201室"
        ));
        
        departments.add(new Department(
            "外科",
            "专治外科疾病及创伤",
            "周一至周五 8:00-17:00",
            40,
            "正常",
            "李医生--主任医师",
            "普外科手术、创伤处理",
            20,
            "门诊楼2层203室"
        ));
        
        departments.add(new Department(
            "儿科",
            "专治儿童常见病、多发病",
            "周一至周日 24小时",
            35,
            "繁忙",
            "王医生--主任医师",
            "儿科疾病、儿童保健",
            30,
            "门诊楼1层101室"
        ));
        
        return departments;
    }

    private void showAddDepartmentDialog() {
        // TODO: 实现添加科室的对话框
    }

    @Override
    public void onDepartmentClick(Department department) {
        // TODO: 实现科室点击事件
    }
} 