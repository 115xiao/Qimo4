package com.example.qimo4.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qimo4.R;
import com.example.qimo4.model.MedicalHistory;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MedicalHistoryAdapter extends RecyclerView.Adapter<MedicalHistoryAdapter.ViewHolder> {
    private List<MedicalHistory> histories;
    private OnItemClickListener listener;
    private SimpleDateFormat dateFormat;

    public interface OnItemClickListener {
        void onViewDetails(MedicalHistory history);
    }

    public MedicalHistoryAdapter() {
        this.histories = new ArrayList<>();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public MedicalHistoryAdapter(OnItemClickListener listener) {
        this();
        this.listener = listener;
    }

    public void setHistories(List<MedicalHistory> histories) {
        this.histories = histories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_medical_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MedicalHistory history = histories.get(position);
        
        // 设置就诊日期
        holder.dateText.setText(history.getVisitDate());
        
        // 设置症状
        holder.symptomsText.setText("症状：" + history.getSymptoms());
        
        // 设置诊断结果
        holder.diagnosisText.setText("诊断：" + history.getDiagnosis());
        
        // 设置既往病史
        holder.historyText.setText("既往史：" + history.getMedicalHistory());

        // 设置点击事件
        if (listener != null) {
            holder.itemView.setOnClickListener(v -> listener.onViewDetails(history));
        }

        // 添加动画效果
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(
                holder.itemView.getContext(), android.R.anim.fade_in));
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }

    public void updateData(List<MedicalHistory> newHistories) {
        this.histories = newHistories;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateText;
        TextView symptomsText;
        TextView diagnosisText;
        TextView historyText;

        ViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.historyDateText);
            symptomsText = view.findViewById(R.id.historySymptomsText);
            diagnosisText = view.findViewById(R.id.historyDiagnosisText);
            historyText = view.findViewById(R.id.historyMedicalHistoryText);
        }
    }
} 