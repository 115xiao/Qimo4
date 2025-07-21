package com.example.qimo4.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qimo4.R;
import com.example.qimo4.adapter.MedicalHistoryAdapter;
import com.example.qimo4.model.MedicalHistory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class PatientHistoryFragment extends Fragment implements MedicalHistoryAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private TextView emptyStateText;
    private ProgressBar loadingProgress;
    private MedicalHistoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化视图
        recyclerView = view.findViewById(R.id.historyRecyclerView);
        emptyStateText = view.findViewById(R.id.emptyStateText);
        loadingProgress = view.findViewById(R.id.loadingProgress);
        
        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MedicalHistoryAdapter(this);
        recyclerView.setAdapter(adapter);
        
        // 加载病历记录
        loadMedicalHistories();
    }

    private void loadMedicalHistories() {
        showLoading();
        
        // TODO: 从服务器加载数据
        // 这里暂时使用模拟数据
        List<MedicalHistory> histories = getMockData();
        
        if (histories.isEmpty()) {
            showEmptyState();
        } else {
            showContent(histories);
        }
    }

    private List<MedicalHistory> getMockData() {
        List<MedicalHistory> mockHistories = new ArrayList<>();
        
        // 添加一些模拟数据
        mockHistories.add(new MedicalHistory(
            "2024-03-20",
            "咳嗽、发热症状，体温38.5℃，持续3天",
            "社区获得性肺炎",
            "否认高血压、糖尿病等慢性病史"
        ));

        mockHistories.add(new MedicalHistory(
            "2024-03-13",
            "腹痛，以上腹部为主，伴有恶心、呕吐，进食后加重",
            "急性胃炎",
            "既往有慢性胃炎病史"
        ));

        mockHistories.add(new MedicalHistory(
            "2024-03-05",
            "头痛、头晕，伴有恶心",
            "偏头痛",
            "有偏头痛病史，平均每月发作1-2次"
        ));

        return mockHistories;
    }

    private void showLoading() {
        loadingProgress.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyStateText.setVisibility(View.GONE);
    }

    private void showContent(List<MedicalHistory> histories) {
        loadingProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
        adapter.setHistories(histories);
    }

    private void showEmptyState() {
        loadingProgress.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyStateText.setVisibility(View.VISIBLE);
    }

    @Override
    public void onViewDetails(MedicalHistory history) {
        View detailView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_medical_history_detail, null);

        // 设置各项详细信息
        TextView visitDateText = detailView.findViewById(R.id.visitDateText);
        TextView symptomsText = detailView.findViewById(R.id.symptomsText);
        TextView diagnosisText = detailView.findViewById(R.id.diagnosisText);
        TextView medicalHistoryText = detailView.findViewById(R.id.medicalHistoryText);

        visitDateText.setText(history.getVisitDate());
        symptomsText.setText(history.getSymptoms());
        diagnosisText.setText(history.getDiagnosis());
        medicalHistoryText.setText(history.getMedicalHistory());

        // 显示详情对话框
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("病历详情")
                .setView(detailView)
                .setPositiveButton("关闭", null)
                .show();
    }
} 