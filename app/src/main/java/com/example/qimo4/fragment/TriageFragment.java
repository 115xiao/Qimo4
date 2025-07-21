package com.example.qimo4.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qimo4.R;
import com.example.qimo4.adapter.TriageRuleAdapter;
import com.example.qimo4.model.TriageRule;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TriageFragment extends Fragment {
    private static final String TAG = "TriageFragment";

    private TextView totalRulesCount;
    private TextView activeRulesCount;
    private TextView pendingRulesCount;
    private TextInputEditText searchInput;
    private ChipGroup filterChipGroup;
    private RecyclerView rulesRecyclerView;
    private ExtendedFloatingActionButton addRuleButton;
    private TriageRuleAdapter ruleAdapter;

    // 所有规则列表
    private final List<TriageRule> allRules = new ArrayList<>();
    // 过滤后的规则列表
    private final List<TriageRule> filteredRules = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 开始创建Fragment视图");
        try {
            View view = inflater.inflate(R.layout.fragment_triage, container, false);
            Log.d(TAG, "onCreateView: 布局加载成功");

            initViews(view);
            setupRecyclerView();
            loadDefaultRules();
            setupListeners();
            updateStatistics();

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
            totalRulesCount = view.findViewById(R.id.totalRulesCount);
            activeRulesCount = view.findViewById(R.id.activeRulesCount);
            pendingRulesCount = view.findViewById(R.id.pendingRulesCount);
            searchInput = view.findViewById(R.id.searchInput);
            filterChipGroup = view.findViewById(R.id.filterChipGroup);
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
            ruleAdapter = new TriageRuleAdapter(filteredRules);
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
            TriageRule rule1 = new TriageRule("胸痛、呼吸困难", "血压>180/110mmHg");
            rule1.setTriageLevel(1);
            rule1.setPriority(10);
            rule1.setTargetDepartment("急诊科");
            allRules.add(rule1);

            TriageRule rule2 = new TriageRule("发热、咳嗽", "体温>38.5℃");
            rule2.setTriageLevel(3);
            rule2.setPriority(6);
            rule2.setTargetDepartment("呼吸内科");
            allRules.add(rule2);

            TriageRule rule3 = new TriageRule("腹痛、恶心呕吐", "无明显异常");
            rule3.setTriageLevel(4);
            rule3.setPriority(4);
            rule3.setTargetDepartment("消化内科");
            rule3.setActive(false);
            allRules.add(rule3);

            // 更新过滤后的列表
            filteredRules.addAll(allRules);
            ruleAdapter.notifyDataSetChanged();

            Log.d(TAG, "loadDefaultRules: 默认分诊规则加载完成，共" + allRules.size() + "条规则");
        } catch (Exception e) {
            Log.e(TAG, "loadDefaultRules: 默认分诊规则加载失败", e);
            throw e;
        }
    }

    private void setupListeners() {
        Log.d(TAG, "setupListeners: 开始设置监听器");
        try {
            // 搜索框监听
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    filterRules();
                }
            });

            // 筛选器监听
            filterChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> filterRules());

            // 添加规则按钮监听
            addRuleButton.setOnClickListener(v -> {
                // TODO: 实现添加规则的功能
                Toast.makeText(getContext(), "添加规则功能开发中", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "setupListeners: 点击了添加规则按钮");
            });

            Log.d(TAG, "setupListeners: 监听器设置完成");
        } catch (Exception e) {
            Log.e(TAG, "setupListeners: 监听器设置失败", e);
            throw e;
        }
    }

    private void filterRules() {
        Log.d(TAG, "filterRules: 开始过滤规则");
        try {
            String searchText = searchInput.getText().toString().toLowerCase().trim();
            List<Integer> checkedChipIds = filterChipGroup.getCheckedChipIds();

            // 根据搜索文本和选中的筛选条件过滤规则
            List<TriageRule> result = allRules.stream()
                .filter(rule -> {
                    // 搜索文本匹配
                    boolean matchesSearch = searchText.isEmpty() ||
                        rule.getSymptoms().toLowerCase().contains(searchText) ||
                        rule.getVitalSigns().toLowerCase().contains(searchText) ||
                        rule.getTargetDepartment().toLowerCase().contains(searchText);

                    // 筛选条件匹配
                    boolean matchesFilter = checkedChipIds.isEmpty();
                    for (int chipId : checkedChipIds) {
                        Chip chip = filterChipGroup.findViewById(chipId);
                        if (chip != null) {
                            String filterText = chip.getText().toString();
                            switch (filterText) {
                                case "一级":
                                    matchesFilter |= rule.getTriageLevel() == 1;
                                    break;
                                case "二级":
                                    matchesFilter |= rule.getTriageLevel() == 2;
                                    break;
                                case "三级":
                                    matchesFilter |= rule.getTriageLevel() == 3;
                                    break;
                                case "四级":
                                    matchesFilter |= rule.getTriageLevel() == 4;
                                    break;
                                case "活跃":
                                    matchesFilter |= rule.isActive();
                                    break;
                                case "待审核":
                                    matchesFilter |= !rule.isActive();
                                    break;
                            }
                        }
                    }

                    return matchesSearch && matchesFilter;
                })
                .collect(Collectors.toList());

            // 更新过滤后的列表
            filteredRules.clear();
            filteredRules.addAll(result);
            ruleAdapter.notifyDataSetChanged();

            // 更新统计数据
            updateStatistics();

            Log.d(TAG, "filterRules: 规则过滤完成，过滤后共" + filteredRules.size() + "条规则");
        } catch (Exception e) {
            Log.e(TAG, "filterRules: 规则过滤失败", e);
            throw e;
        }
    }

    private void updateStatistics() {
        Log.d(TAG, "updateStatistics: 开始更新统计数据");
        try {
            int total = allRules.size();
            long active = allRules.stream().filter(TriageRule::isActive).count();
            long pending = total - active;

            totalRulesCount.setText(String.valueOf(total));
            activeRulesCount.setText(String.valueOf(active));
            pendingRulesCount.setText(String.valueOf(pending));

            Log.d(TAG, "updateStatistics: 统计数据更新完成");
        } catch (Exception e) {
            Log.e(TAG, "updateStatistics: 统计数据更新失败", e);
            throw e;
        }
    }
}