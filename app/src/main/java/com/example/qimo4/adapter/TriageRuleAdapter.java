package com.example.qimo4.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qimo4.R;
import com.example.qimo4.model.TriageRule;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

public class TriageRuleAdapter extends RecyclerView.Adapter<TriageRuleAdapter.ViewHolder> {
    private static final String TAG = "TriageRuleAdapter";

    private final List<TriageRule> rules;
    private OnRuleActionListener actionListener;

    public interface OnRuleActionListener {
        void onRuleEdit(int position, TriageRule rule);
        void onRuleDelete(int position);
        void onRuleActiveChanged(int position, boolean isActive);
    }

    public TriageRuleAdapter(List<TriageRule> rules) {
        this.rules = rules;
    }

    public void setOnRuleActionListener(OnRuleActionListener listener) {
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: 创建ViewHolder");
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_triage_rule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: 绑定ViewHolder，位置：" + position);
        try {
            TriageRule rule = rules.get(position);

            // 设置规则ID
            holder.ruleIdText.setText(String.format("规则 #%03d", position + 1));

            // 设置状态
            holder.statusChip.setText(rule.isActive() ? "活跃" : "待审核");
            holder.statusChip.setChipBackgroundColorResource(
                rule.isActive() ? android.R.color.holo_green_light : android.R.color.holo_orange_light
            );

            // 设置分诊等级
            holder.triageLevelText.setText(rule.getTriageLevelDescription());
            int textColorRes;
            switch (rule.getTriageLevel()) {
                case 1:
                    textColorRes = android.R.color.holo_red_dark;
                    break;
                case 2:
                    textColorRes = android.R.color.holo_orange_dark;
                    break;
                case 3:
                    textColorRes = android.R.color.holo_blue_dark;
                    break;
                default:
                    textColorRes = android.R.color.darker_gray;
            }
            holder.triageLevelText.setTextColor(holder.itemView.getContext().getColor(textColorRes));

            // 设置症状和生命体征
            holder.symptomsText.setText(rule.getSymptoms());
            holder.vitalSignsText.setText(rule.getVitalSigns());
            holder.departmentText.setText(rule.getTargetDepartment());

            // 设置按钮状态
            holder.toggleButton.setText(rule.isActive() ? "停用" : "启用");
            holder.toggleButton.setOnClickListener(v -> {
                Log.d(TAG, "onBindViewHolder: 切换规则状态，位置：" + position);
                rule.setActive(!rule.isActive());
                if (actionListener != null) {
                    actionListener.onRuleActiveChanged(position, rule.isActive());
                }
                notifyItemChanged(position);
            });

            holder.editButton.setOnClickListener(v -> {
                Log.d(TAG, "onBindViewHolder: 编辑规则，位置：" + position);
                if (actionListener != null) {
                    actionListener.onRuleEdit(position, rule);
                }
            });

            Log.d(TAG, "onBindViewHolder: 规则数据绑定完成，位置：" + position);
        } catch (Exception e) {
            Log.e(TAG, "onBindViewHolder: 规则数据绑定失败，位置：" + position, e);
        }
    }

    @Override
    public int getItemCount() {
        return rules.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView ruleIdText;
        final Chip statusChip;
        final TextView triageLevelText;
        final TextView symptomsText;
        final TextView vitalSignsText;
        final TextView departmentText;
        final MaterialButton editButton;
        final MaterialButton toggleButton;

        ViewHolder(View view) {
            super(view);
            ruleIdText = view.findViewById(R.id.ruleIdText);
            statusChip = view.findViewById(R.id.statusChip);
            triageLevelText = view.findViewById(R.id.triageLevelText);
            symptomsText = view.findViewById(R.id.symptomsText);
            vitalSignsText = view.findViewById(R.id.vitalSignsText);
            departmentText = view.findViewById(R.id.departmentText);
            editButton = view.findViewById(R.id.editButton);
            toggleButton = view.findViewById(R.id.toggleButton);
        }
    }
}