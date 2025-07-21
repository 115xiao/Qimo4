package com.example.qimo4.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qimo4.R;
import com.example.qimo4.adapter.TriageRuleAdapter;
import com.example.qimo4.model.TriageRule;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TriageAlgorithmFragment extends Fragment implements TriageRuleAdapter.OnRuleActionListener {
    private static final String TAG = "TriageAlgorithmFragment";

    private RecyclerView rulesRecyclerView;
    private ExtendedFloatingActionButton addRuleButton;
    private TriageRuleAdapter ruleAdapter;
    private final List<TriageRule> rules = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 开始创建Fragment视图");
        try {
            View view = inflater.inflate(R.layout.fragment_triage_algorithm, container, false);
            Log.d(TAG, "onCreateView: 布局加载成功");

        initViews(view);
            setupRecyclerView();
            loadDefaultRules();
        setupListeners();

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
            rulesRecyclerView = view.findViewById(R.id.rulesRecyclerView);
            addRuleButton = view.findViewById(R.id.addRuleButton);
            Log.d(TAG, "initViews: 视图组件初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "initViews: 视图初始化失败", e);
            throw e;
        }
    }

    private void setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: 开始设置RecyclerView");
        try {
            ruleAdapter = new TriageRuleAdapter(rules);
            rulesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            rulesRecyclerView.setAdapter(ruleAdapter);
            Log.d(TAG, "setupRecyclerView: RecyclerView设置完成");
        } catch (Exception e) {
            Log.e(TAG, "setupRecyclerView: RecyclerView设置失败", e);
            throw e;
        }
    }

    private void loadDefaultRules() {
        Log.d(TAG, "loadDefaultRules: 开始加载默认分诊规则");
        try {
            // 添加一些预设的分诊规则
            addRule(createRule(1, "胸痛、呼吸困难", "血压>180/110mmHg", "急诊科"));
            addRule(createRule(2, "意识障碍", "GCS<8分", "急诊科"));
            addRule(createRule(3, "发热、咳嗽", "体温>38.5℃", "呼吸内科"));
            addRule(createRule(4, "腹痛、恶心呕吐", "无明显异常", "消化内科"));

            Log.d(TAG, "loadDefaultRules: 默认分诊规则加载完成，共" + rules.size() + "条规则");
        } catch (Exception e) {
            Log.e(TAG, "loadDefaultRules: 默认分诊规则加载失败", e);
            throw e;
        }
    }

    private TriageRule createRule(int level, String symptoms, String vitalSigns, String department) {
        TriageRule rule = new TriageRule(symptoms, vitalSigns);
        rule.setTriageLevel(level);
        rule.setTargetDepartment(department);
        rule.setPriority(5 - level);
        return rule;
    }

    private void setupListeners() {
        Log.d(TAG, "setupListeners: 开始设置监听器");
        try {
            addRuleButton.setOnClickListener(v -> {
                Toast.makeText(getContext(), "添加规则功能开发中", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "setupListeners: 点击了添加规则按钮");
            });
            Log.d(TAG, "setupListeners: 监听器设置完成");
        } catch (Exception e) {
            Log.e(TAG, "setupListeners: 监听器设置失败", e);
            throw e;
        }
    }

    @Override
    public void onRuleEdit(int position, TriageRule rule) {
        Log.d(TAG, "onRuleEdit: 编辑规则，位置：" + position);
        Toast.makeText(getContext(), "编辑规则功能开发中", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRuleDelete(int position) {
        Log.d(TAG, "onRuleDelete: 删除规则，位置：" + position);
        removeRule(position);
    }

    @Override
    public void onRuleActiveChanged(int position, boolean isActive) {
        Log.d(TAG, "onRuleActiveChanged: 更改规则状态，位置：" + position + "，状态：" + isActive);
        updateRule(position, rules.get(position));
    }

    private void addRule(TriageRule rule) {
        rules.add(rule);
        ruleAdapter.notifyItemInserted(rules.size() - 1);
    }

    private void updateRule(int position, TriageRule rule) {
        if (position >= 0 && position < rules.size()) {
            rules.set(position, rule);
            ruleAdapter.notifyItemChanged(position);
        }
    }

    private void removeRule(int position) {
        if (position >= 0 && position < rules.size()) {
            rules.remove(position);
            ruleAdapter.notifyItemRemoved(position);
        }
    }

    public List<TriageRule> getRules() {
        return new ArrayList<>(rules);
    }
} 