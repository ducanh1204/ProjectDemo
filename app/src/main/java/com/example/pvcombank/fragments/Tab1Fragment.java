package com.example.pvcombank.fragments;

import android.view.View;
import android.widget.TextView;

import com.example.pvcombank.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class Tab1Fragment extends BaseFragment {

    private LinearProgressIndicator linearProgressIndicator;
    private TextView tvTitleStep,tvDescription;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_tab1;
    }

    @Override
    protected void loadControlsAndResize(View view) {
        linearProgressIndicator = view.findViewById(R.id.linear_progress_indicator);
        tvTitleStep = view.findViewById(R.id.tv_title_step);
        tvDescription = view.findViewById(R.id.tv_description);
        mainActivity.addOrReplaceFragment(R.id.content_steps,new Step5Fragment(linearProgressIndicator,tvTitleStep,tvDescription),false,false);
    }
}