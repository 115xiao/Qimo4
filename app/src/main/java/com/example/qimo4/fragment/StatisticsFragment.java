package com.example.qimo4.fragment;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.qimo4.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticsFragment extends Fragment {
    private MaterialButton startDateButton;
    private MaterialButton endDateButton;
    private PieChart triageLevelPieChart;
    private BarChart departmentBarChart;
    private LineChart waitingTimeLineChart;
    private TableLayout accuracyTable;
    private Calendar startDate;
    private Calendar endDate;
    private SimpleDateFormat dateFormat;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        initViews(view);
        setupDatePickers();
        setupCharts();
        loadMockData(); // 加载模拟数据
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 在这里初始化视图组件
    }

    private void initViews(View view) {
        startDateButton = view.findViewById(R.id.startDateButton);
        endDateButton = view.findViewById(R.id.endDateButton);
        triageLevelPieChart = view.findViewById(R.id.triageLevelPieChart);
        departmentBarChart = view.findViewById(R.id.departmentBarChart);
        waitingTimeLineChart = view.findViewById(R.id.waitingTimeLineChart);
        accuracyTable = view.findViewById(R.id.accuracyTable);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        startDate = Calendar.getInstance();
        endDate = Calendar.getInstance();

        // 设置快捷选择按钮
        Chip todayChip = view.findViewById(R.id.todayChip);
        Chip weekChip = view.findViewById(R.id.weekChip);
        Chip monthChip = view.findViewById(R.id.monthChip);

        todayChip.setOnClickListener(v -> setTimeRange(0));
        weekChip.setOnClickListener(v -> setTimeRange(7));
        monthChip.setOnClickListener(v -> setTimeRange(30));
    }

    private void setupDatePickers() {
        startDateButton.setText(dateFormat.format(startDate.getTime()));
        endDateButton.setText(dateFormat.format(endDate.getTime()));

        startDateButton.setOnClickListener(v -> showDatePicker(true));
        endDateButton.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isStartDate) {
        Calendar calendar = isStartDate ? startDate : endDate;
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            requireContext(),
            (view, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                (isStartDate ? startDateButton : endDateButton).setText(dateFormat.format(calendar.getTime()));
                loadMockData(); // 重新加载数据
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setTimeRange(int days) {
        endDate = Calendar.getInstance();
        startDate = Calendar.getInstance();
        startDate.add(Calendar.DAY_OF_MONTH, -days);
        
        startDateButton.setText(dateFormat.format(startDate.getTime()));
        endDateButton.setText(dateFormat.format(endDate.getTime()));
        
        loadMockData(); // 重新加载数据
    }

    private void setupCharts() {
        // 设置分诊等级饼图
        triageLevelPieChart.getDescription().setEnabled(false);
        triageLevelPieChart.setDrawHoleEnabled(true);
        triageLevelPieChart.setHoleColor(Color.WHITE);
        triageLevelPieChart.setTransparentCircleRadius(61f);
        triageLevelPieChart.setDrawCenterText(true);
        triageLevelPieChart.setCenterText("分诊等级分布");

        // 设置科室分布柱状图
        departmentBarChart.getDescription().setEnabled(false);
        departmentBarChart.setDrawGridBackground(false);
        departmentBarChart.setDrawBarShadow(false);
        departmentBarChart.setHighlightFullBarEnabled(false);
        XAxis xAxis = departmentBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        YAxis leftAxis = departmentBarChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        departmentBarChart.getAxisRight().setEnabled(false);

        // 设置等待时间折线图
        waitingTimeLineChart.getDescription().setEnabled(false);
        waitingTimeLineChart.setDrawGridBackground(false);
        XAxis waitingTimeXAxis = waitingTimeLineChart.getXAxis();
        waitingTimeXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        waitingTimeXAxis.setDrawGridLines(false);
        YAxis waitingTimeLeftAxis = waitingTimeLineChart.getAxisLeft();
        waitingTimeLeftAxis.setDrawGridLines(true);
        waitingTimeLineChart.getAxisRight().setEnabled(false);
    }

    private void loadMockData() {
        // 加载分诊等级分布数据
        List<PieEntry> triageLevelEntries = new ArrayList<>();
        triageLevelEntries.add(new PieEntry(30f, "一级"));
        triageLevelEntries.add(new PieEntry(45f, "二级"));
        triageLevelEntries.add(new PieEntry(15f, "三级"));
        triageLevelEntries.add(new PieEntry(10f, "四级"));

        PieDataSet triageLevelDataSet = new PieDataSet(triageLevelEntries, "分诊等级");
        triageLevelDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        triageLevelDataSet.setValueTextSize(12f);
        triageLevelDataSet.setValueTextColor(Color.WHITE);

        PieData triageLevelData = new PieData(triageLevelDataSet);
        triageLevelPieChart.setData(triageLevelData);
        triageLevelPieChart.invalidate();

        // 加载科室分布数据
        List<BarEntry> departmentEntries = new ArrayList<>();
        departmentEntries.add(new BarEntry(0, 45f));
        departmentEntries.add(new BarEntry(1, 30f));
        departmentEntries.add(new BarEntry(2, 25f));
        departmentEntries.add(new BarEntry(3, 20f));
        departmentEntries.add(new BarEntry(4, 15f));

        BarDataSet departmentDataSet = new BarDataSet(departmentEntries, "就诊人数");
        departmentDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData departmentData = new BarData(departmentDataSet);
        departmentBarChart.setData(departmentData);

        // 设置X轴标签
        final String[] departments = new String[]{"内科", "外科", "儿科", "妇产科", "骨科"};
        XAxis departmentXAxis = departmentBarChart.getXAxis();
        departmentXAxis.setValueFormatter(new IndexAxisValueFormatter(departments));
        departmentXAxis.setLabelRotationAngle(-45);

        departmentBarChart.invalidate();

        // 加载等待时间数据
        List<Entry> waitingTimeEntries = new ArrayList<>();
        waitingTimeEntries.add(new Entry(0, 15f));
        waitingTimeEntries.add(new Entry(1, 25f));
        waitingTimeEntries.add(new Entry(2, 35f));
        waitingTimeEntries.add(new Entry(3, 20f));
        waitingTimeEntries.add(new Entry(4, 30f));

        LineDataSet waitingTimeDataSet = new LineDataSet(waitingTimeEntries, "平均等待时间(分钟)");
        waitingTimeDataSet.setColor(Color.BLUE);
        waitingTimeDataSet.setCircleColor(Color.BLUE);
        waitingTimeDataSet.setLineWidth(2f);
        waitingTimeDataSet.setCircleRadius(4f);
        waitingTimeDataSet.setDrawValues(true);

        LineData waitingTimeData = new LineData(waitingTimeDataSet);
        waitingTimeLineChart.setData(waitingTimeData);

        // 设置X轴标签
        final String[] timeSlots = new String[]{"8:00", "10:00", "12:00", "14:00", "16:00"};
        XAxis waitingTimeXAxis = waitingTimeLineChart.getXAxis();
        waitingTimeXAxis.setValueFormatter(new IndexAxisValueFormatter(timeSlots));
        waitingTimeXAxis.setLabelRotationAngle(-45);

        waitingTimeLineChart.invalidate();

        // 加载准确率统计表格
        loadAccuracyTable();
    }

    private void loadAccuracyTable() {
        // 清除旧数据（保留表头）
        int childCount = accuracyTable.getChildCount();
        if (childCount > 1) {
            accuracyTable.removeViews(1, childCount - 1);
        }

        // 模拟数据
        String[][] data = {
            {"内科", "100", "90", "90%"},
            {"外科", "80", "70", "87.5%"},
            {"儿科", "60", "55", "91.7%"},
            {"妇产科", "40", "35", "87.5%"},
            {"骨科", "30", "28", "93.3%"}
        };

        // 添加数据行
        for (String[] row : data) {
            TableRow tableRow = new TableRow(requireContext());
            tableRow.setPadding(8, 8, 8, 8);

            for (String cell : row) {
                TextView textView = new TextView(requireContext());
                textView.setText(cell);
                textView.setPadding(8, 8, 8, 8);
                tableRow.addView(textView);
            }

            accuracyTable.addView(tableRow);
        }
    }
} 