package com.example.qimo4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qimo4.R;
import com.example.qimo4.model.Department;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class DepartmentAdapter extends RecyclerView.Adapter<DepartmentAdapter.ViewHolder> {
    private List<Department> departments;
    private final OnDepartmentClickListener listener;

    public interface OnDepartmentClickListener {
        void onDepartmentClick(Department department);
    }

    public DepartmentAdapter(OnDepartmentClickListener listener) {
        this.departments = new ArrayList<>();
        this.listener = listener;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
        notifyDataSetChanged();
    }

    public List<Department> getDepartments() {
        return departments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_department, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Department department = departments.get(position);
        
        // 设置科室基本信息
        holder.nameText.setText(department.getName());
        holder.descriptionText.setText(department.getDescription());
        holder.scheduleText.setText(department.getSchedule());
        holder.feeText.setText("挂号费：" + department.getFee() + "元");
        
        // 设置科室状态
        holder.statusChip.setText(department.getStatus());
        
        // 设置等待时间
        holder.waitingTimeText.setText("等待时间：" + department.getWaitingTime() + "分钟");
        
        // 设置医生信息
        holder.doctorText.setText(department.getDoctor());
        
        // 设置位置信息
        holder.locationText.setText("位置：" + department.getLocation());
        
        // 设置医生专长
        holder.expertiseText.setText(department.getExpertise());

        // 添加动画效果
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(
                holder.itemView.getContext(), android.R.anim.fade_in));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDepartmentClick(department);
            }
        });
    }

    @Override
    public int getItemCount() {
        return departments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView descriptionText;
        TextView scheduleText;
        TextView feeText;
        Chip statusChip;
        TextView waitingTimeText;
        TextView doctorText;
        TextView locationText;
        TextView expertiseText;

        ViewHolder(View view) {
            super(view);
            nameText = view.findViewById(R.id.departmentNameText);
            descriptionText = view.findViewById(R.id.departmentDescriptionText);
            scheduleText = view.findViewById(R.id.departmentScheduleText);
            feeText = view.findViewById(R.id.departmentFeeText);
            statusChip = view.findViewById(R.id.statusChip);
            waitingTimeText = view.findViewById(R.id.waitingTimeText);
            doctorText = view.findViewById(R.id.doctorText);
            locationText = view.findViewById(R.id.locationText);
            expertiseText = view.findViewById(R.id.expertiseText);
        }
    }
} 