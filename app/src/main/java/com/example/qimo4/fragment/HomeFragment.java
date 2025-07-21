package com.example.qimo4.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qimo4.R;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: 开始创建Fragment视图");
        try {
            View view = inflater.inflate(R.layout.fragment_home, container, false);
            Log.d(TAG, "onCreateView: 布局加载成功");
            initViews(view);
            Log.d(TAG, "onCreateView: Fragment初始化完成");
            return view;
        } catch (Exception e) {
            Log.e(TAG, "onCreateView: Fragment创建失败", e);
            throw e;
        }
    }

    private void initViews(View view) {
        try {
            Log.d(TAG, "initViews: 开始初始化视图组件");
            TextView titleText = view.findViewById(R.id.titleText);
            titleText.setText("首页");
            Log.d(TAG, "initViews: 视图组件初始化完成");
        } catch (Exception e) {
            Log.e(TAG, "initViews: 视图初始化失败", e);
            throw e;
        }
    }
} 