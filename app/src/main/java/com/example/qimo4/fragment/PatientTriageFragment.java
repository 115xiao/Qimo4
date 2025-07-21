package com.example.qimo4.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioButton;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qimo4.R;
import com.example.qimo4.adapter.DepartmentAdapter;
import com.example.qimo4.adapter.MedicalHistoryAdapter;
import com.example.qimo4.model.Department;
import com.example.qimo4.model.MedicalHistory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.radiobutton.MaterialRadioButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.util.Map;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import java.util.Collections;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.SharedPreferences;

public class PatientTriageFragment extends Fragment implements DepartmentAdapter.OnDepartmentClickListener {
    private static final String TAG = "PatientTriageFragment";
    private static int appointmentNumber = 1;  // 预约号码，从1开始

    // 视图组件
    private RecyclerView departmentRecyclerView;
    private MaterialButton startTriageButton;
    private MaterialButton viewQrCodeButton;
    private TextView nameTextView;
    private TextView idCardTextView;
    private TextView phoneTextView;
    private TextInputEditText ageInput;
    private RadioGroup genderRadioGroup;
    private RadioButton maleRadio;
    private RadioGroup bodyPartRadioGroup;
    private TextView specificSymptomsLabel;
    private RadioGroup specificSymptomsRadioGroup;
    private RadioGroup onsetTimeRadioGroup;
    private RadioGroup severityRadioGroup;
    private TextInputEditText medicalHistoryInput;
    private MaterialButton loadHistoryButton;
    private RecyclerView historyRecyclerView;
    private TextView noHistoryText;
    private View recommendationCard;
    private View appointmentCard;
    private TextView queueNumberText;
    private TextView estimatedTimeText;
    private TextView locationText;

    // 适配器
    private DepartmentAdapter departmentAdapter;
    private MedicalHistoryAdapter historyAdapter;
    private List<String> commonSymptoms = Arrays.asList(
        "发热", "头痛", "咳嗽", "咽痛", "腹痛", "恶心呕吐", 
        "腹泻", "胸痛", "呼吸困难", "关节疼痛", "皮疹", 
        "头晕", "视力模糊", "耳鸣", "心悸"
    );

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: Fragment附加到Activity");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Fragment创建");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 开始创建Fragment视图");
        try {
            View view = inflater.inflate(R.layout.fragment_patient_triage, container, false);
            initViews(view);
            setupSymptomChips();
            setupRecyclerViews();
            setupClickListeners();
            return view;
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Fragment创建失败", e);
            throw e;
        }
    }

    private void initViews(View view) {
        Log.d(TAG, "initViews: 初始化视图组件");
        try {
            // 基本信息显示组件
            nameTextView = view.findViewById(R.id.nameTextView);
            idCardTextView = view.findViewById(R.id.idCardTextView);
            phoneTextView = view.findViewById(R.id.phoneTextView);
            ageInput = view.findViewById(R.id.ageInput);
            genderRadioGroup = view.findViewById(R.id.genderRadioGroup);
            maleRadio = view.findViewById(R.id.maleRadio);

            // 设置默认值
            ageInput.setText("18");
            maleRadio.setChecked(true);

            // 症状选择
            bodyPartRadioGroup = view.findViewById(R.id.bodyPartRadioGroup);
            specificSymptomsLabel = view.findViewById(R.id.specificSymptomsLabel);
            specificSymptomsRadioGroup = view.findViewById(R.id.specificSymptomsRadioGroup);
            onsetTimeRadioGroup = view.findViewById(R.id.onsetTimeRadioGroup);
            severityRadioGroup = view.findViewById(R.id.severityRadioGroup);
            medicalHistoryInput = view.findViewById(R.id.medicalHistoryInput);

            // 病历记录
            loadHistoryButton = view.findViewById(R.id.loadHistoryButton);
            historyRecyclerView = view.findViewById(R.id.historyRecyclerView);
            noHistoryText = view.findViewById(R.id.noHistoryText);

            // 推荐科室
            recommendationCard = view.findViewById(R.id.recommendationCard);
            departmentRecyclerView = view.findViewById(R.id.departmentRecyclerView);

            // 预约信息
            appointmentCard = view.findViewById(R.id.appointmentCard);
            queueNumberText = view.findViewById(R.id.queueNumberText);
            estimatedTimeText = view.findViewById(R.id.estimatedTimeText);
            locationText = view.findViewById(R.id.locationText);

            // 按钮
            startTriageButton = view.findViewById(R.id.startTriageButton);
            viewQrCodeButton = view.findViewById(R.id.viewQrCodeButton);

            // 加载用户信息
            loadUserInfo();

            Log.d(TAG, "initViews: 视图组件初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "initViews: 视图初始化失败", e);
            throw e;
        }
    }

    private void setupSymptomChips() {
        for (String symptom : commonSymptoms) {
            Chip chip = new Chip(requireContext());
            chip.setText(symptom);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(true);
            specificSymptomsRadioGroup.addView(chip);
        }
    }

    private void setupRecyclerViews() {
        // 设置科室列表
        departmentAdapter = new DepartmentAdapter(this);
        departmentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        departmentRecyclerView.setAdapter(departmentAdapter);

        // 设置病历列表
        historyAdapter = new MedicalHistoryAdapter();
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        historyRecyclerView.setAdapter(historyAdapter);
    }

    private void setupClickListeners() {
        loadHistoryButton.setOnClickListener(v -> loadMedicalHistory());
        startTriageButton.setOnClickListener(v -> startTriage());
        viewQrCodeButton.setOnClickListener(v -> showQrCode());
    }

    private void loadMedicalHistory() {
        String idCard = idCardTextView.getText().toString();
        if (idCard.isEmpty()) {
            Toast.makeText(requireContext(), "请先输入身份证号", Toast.LENGTH_SHORT).show();
            return;
        }

        // 设置固定的既往史内容
        medicalHistoryInput.setText("有高血压病史，长期服用降压药");
        
        // 隐藏历史病历相关视图
        historyRecyclerView.setVisibility(View.GONE);
        noHistoryText.setVisibility(View.GONE);
    }

    private void startTriage() {
        if (!validateInput()) {
            return;
        }

        // 获取选中的具体症状
        String selectedSymptom = "";
        for (int i = 0; i < specificSymptomsRadioGroup.getChildCount(); i++) {
            RadioButton radioButton = (RadioButton) specificSymptomsRadioGroup.getChildAt(i);
            if (radioButton.isChecked()) {
                selectedSymptom = radioButton.getText().toString();
                break;
            }
        }

        // 将单个症状转换为List
        List<String> symptoms = new ArrayList<>();
        symptoms.add(selectedSymptom);

        // 根据症状推荐科室
        List<Department> recommendedDepartments = getRecommendedDepartments(symptoms);
        departmentAdapter.setDepartments(recommendedDepartments);
        
        // 显示推荐科室卡片
        recommendationCard.setVisibility(View.VISIBLE);
        recommendationCard.startAnimation(AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left));

        // 隐藏开始分诊按钮
        startTriageButton.setVisibility(View.GONE);
    }

    private boolean validateInput() {
        if (nameTextView.getText().toString().isEmpty()) {
            showError("请输入姓名");
            return false;
        }
        if (idCardTextView.getText().toString().isEmpty()) {
            showError("请输入身份证号");
            return false;
        }
        if (phoneTextView.getText().toString().isEmpty()) {
            showError("请输入手机号");
            return false;
        }
        if (ageInput.getText().toString().isEmpty()) {
            showError("请输入年龄");
            return false;
        }
        if (genderRadioGroup.getCheckedRadioButtonId() == -1) {
            showError("请选择性别");
            return false;
        }
        if (bodyPartRadioGroup.getCheckedRadioButtonId() == -1) {
            showError("请选择身体部位");
            return false;
        }
        if (onsetTimeRadioGroup.getCheckedRadioButtonId() == -1) {
            showError("请选择发病时间");
            return false;
        }
        if (severityRadioGroup.getCheckedRadioButtonId() == -1) {
            showError("请选择症状程度");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private List<Department> getRecommendedDepartments(List<String> symptoms) {
        // 根据症状推荐科室，这里使用模拟数据
        List<Department> departments = new ArrayList<>();
        if (symptoms.contains("发热") || symptoms.contains("咳嗽") || symptoms.contains("咽痛")) {
            // 呼吸内科
        departments.add(new Department(
            "呼吸内科", 
            "专治呼吸系统疾病", 
            "上午8:00-11:30，下午14:00-17:30", 
            25,
            "正常",
            "张医生--主任医师",
            "呼吸系统疾病、哮喘、慢性支气管炎",
            15,
            "门诊楼2层202室"
        ));
        }
        if (symptoms.contains("腹痛") || symptoms.contains("恶心呕吐") || symptoms.contains("腹泻")) {
            // 消化内科
            departments.add(new Department(
            "消化内科", 
            "专治消化系统疾病", 
            "上午8:00-11:30，下午14:00-17:30", 
            30,
            "繁忙",
            "李医生--副主任医师",
            "胃病、肠道疾病、消化系统炎症",
            30,
            "门诊楼2层205室"
        ));
        }
        if (symptoms.contains("胸痛") || symptoms.contains("心悸")) {
            // 心内科
            departments.add(new Department(
            "心内科", 
            "专治心血管疾病", 
            "上午8:00-11:30，下午14:00-17:30", 
            35,
            "正常",
            "王医生--主任医师",
            "心血管疾病、高血压、冠心病",
            20,
            "门诊楼3层302室"
        ));
        }
        if (symptoms.contains("头痛") || symptoms.contains("头晕")) {
            // 神经内科
            departments.add(new Department(
            "神经内科", 
            "专治神经系统疾病", 
            "上午8:00-11:30，下午14:00-17:30", 
            40,
            "空闲",
            "刘医生--主任医师",
            "头痛、眩晕、神经系统疾病",
            5,
            "门诊楼3层305室"
        ));
        }
        

        return departments;
    }

    @Override
    public void onDepartmentClick(Department department) {
        new AlertDialog.Builder(requireContext())
            .setTitle("确认预约")
            .setMessage("您确定要预约" + department.getName() + "吗？\n\n" +
                    "科室特长：" + department.getDescription() + "\n" +
                    "坐诊时间：" + department.getSchedule() + "\n" +
                    "挂号费：" + department.getFee() + "元\n\n" +
                    "确认后将自动扣除挂号费。")
            .setPositiveButton("确认预约", (dialog, which) -> {
                // 模拟预约成功
                recommendationCard.setVisibility(View.GONE);
                appointmentCard.setVisibility(View.VISIBLE);
                appointmentCard.startAnimation(AnimationUtils.loadAnimation(requireContext(), android.R.anim.slide_in_left));

                // 显示预约信息
                queueNumberText.setText("您的队列号：A123");
                estimatedTimeText.setText("预计等待时间：约30分钟");
                locationText.setText("就诊地点：门诊楼5层 " + department.getName());
            })
            .setNegativeButton("取消", null)
            .show();
    }

    private void showQrCode() {
        try {
            // 获取当前选中的科室
            Department selectedDepartment = departmentAdapter.getDepartments().get(0);
            
            // 获取当前时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String currentTime = dateFormat.format(new Date());
            
            // 生成预约信息字符串
            String content = String.format(
                "就诊信息\n" +
                "------------------------\n" +
                "姓名：%s\n" +
                "身份证号：%s\n" +
                "手机号：%s\n" +
                "年龄：%s\n" +
                "性别：%s\n" +
                "------------------------\n" +
                "科室：%s\n" +
                "医生：%s\n" +
                "位置：%s\n" +
                "------------------------\n" +
                "预约编号：AP%s%04d",
                nameTextView.getText().toString(),
                idCardTextView.getText().toString(),
                phoneTextView.getText().toString(),
                ageInput.getText().toString(),
                ((RadioButton)genderRadioGroup.findViewById(genderRadioGroup.getCheckedRadioButtonId())).getText().toString(),
                selectedDepartment.getName(),
                selectedDepartment.getDoctor(),
                selectedDepartment.getLocation(),
                getCurrentDate(),
                appointmentNumber
            );

            // 创建二维码位图
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(
                content,
                BarcodeFormat.QR_CODE,
                512,
                512,
                Collections.singletonMap(EncodeHintType.CHARACTER_SET, "UTF-8")
            );

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            // 创建包含二维码的ImageView
            ImageView qrCodeImageView = new ImageView(requireContext());
            qrCodeImageView.setImageBitmap(bitmap);
            qrCodeImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            // 设置ImageView的布局参数
            int size = (int) (getResources().getDisplayMetrics().density * 250); // 250dp
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.gravity = Gravity.CENTER;
            qrCodeImageView.setLayoutParams(layoutParams);

            // 创建包含说明文字和二维码的容器
            LinearLayout container = new LinearLayout(requireContext());
            container.setOrientation(LinearLayout.VERTICAL);
            container.setGravity(Gravity.CENTER);
            container.setPadding(0, 40, 0, 40);

            // 添加说明文字
            TextView infoText = new TextView(requireContext());
            infoText.setText("请保存此二维码，用于现场报到和查询\n\n" + content);
            infoText.setGravity(Gravity.CENTER);
            infoText.setPadding(20, 0, 20, 20);
            container.addView(infoText);

            // 添加二维码图片
            container.addView(qrCodeImageView);

            // 显示对话框
            new AlertDialog.Builder(requireContext())
                    .setTitle("预约二维码")
                    .setView(container)
                    .setPositiveButton("确定", null)
                    .show();

        } catch (Exception e) {
            Toast.makeText(requireContext(), "生成二维码失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // 初始化视图
        initViews(view);
        setupRecyclerViews();
        setupClickListeners();
        
        // 预填基本信息
        prefillBasicInfo();
        
        // 设置身体部位选择监听器
        setupBodyPartSelection();

        // 清空既往史输入框
        medicalHistoryInput.setText("");
    }

    private void prefillBasicInfo() {
        // 预填写基本信息，保持可编辑状态
        nameTextView.setText("张三");
        idCardTextView.setText("110101199001011234");
        phoneTextView.setText("13800138000");
        
        // 自动加载病历记录
        loadMedicalHistory();
    }

    private void setupBodyPartSelection() {
        bodyPartRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            // 清空并隐藏具体症状选项
            specificSymptomsRadioGroup.removeAllViews();
            specificSymptomsLabel.setVisibility(View.VISIBLE);
            specificSymptomsRadioGroup.setVisibility(View.VISIBLE);

            // 根据选择的身体部位显示对应的具体症状
            List<String> symptoms = getSpecificSymptoms(checkedId);
            for (String symptom : symptoms) {
                MaterialRadioButton radioButton = new MaterialRadioButton(requireContext());
                radioButton.setText(symptom);
                specificSymptomsRadioGroup.addView(radioButton);
            }
        });
    }

    private List<String> getSpecificSymptoms(int bodyPartId) {
        List<String> symptoms = new ArrayList<>();
        if (bodyPartId == R.id.headRadio) {
            symptoms.addAll(Arrays.asList(
                "头痛", "头晕", "恶心", "呕吐",
                "视力模糊", "耳鸣", "听力下降"
            ));
        } else if (bodyPartId == R.id.limbsRadio) {
            symptoms.addAll(Arrays.asList(
                "关节疼痛", "肌肉酸痛", "麻木",
                "肿胀", "活动受限"
            ));
        } else if (bodyPartId == R.id.abdomenRadio) {
            symptoms.addAll(Arrays.asList(
                "腹痛", "腹胀", "恶心呕吐",
                "食欲不振", "便秘", "腹泻"
            ));
        } else if (bodyPartId == R.id.chestRadio) {
            symptoms.addAll(Arrays.asList(
                "胸痛", "胸闷", "呼吸困难",
                "心悸", "咳嗽", "咳痰"
            ));
        } else if (bodyPartId == R.id.backRadio) {
            symptoms.addAll(Arrays.asList(
                "背痛", "腰痛", "活动受限",
                "酸痛", "放射痛"
            ));
        } else if (bodyPartId == R.id.neckRadio) {
            symptoms.addAll(Arrays.asList(
                "颈部疼痛", "活动受限", "僵硬",
                "头晕", "颈部肿胀"
            ));
        }
        return symptoms;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Fragment开始");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: Fragment恢复");
        loadUserInfo();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: Fragment暂停");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Fragment停止");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: Fragment视图销毁");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Fragment销毁");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Fragment从Activity分离");
    }

    private void loadUserInfo() {
        Log.d(TAG, "loadUserInfo: 开始加载用户信息");
        try {
            // 从SharedPreferences加载数据
            SharedPreferences prefs = requireContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String name = prefs.getString("name", "张三");
            String idCard = prefs.getString("idCard", "110101199001011234");
            String phone = prefs.getString("phone", "13800138000");

            // 更新UI
            nameTextView.setText(name);
            idCardTextView.setText(idCard);
            phoneTextView.setText(phone);
            
            Log.d(TAG, "loadUserInfo: 用户信息加载完成");
        } catch (Exception e) {
            Log.e(TAG, "loadUserInfo: 用户信息加载失败", e);
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(new Date());
    }
} 