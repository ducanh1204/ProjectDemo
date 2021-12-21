package com.example.pvcombank.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pvcombank.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class Step5Fragment extends BaseFragment {


    private LinearProgressIndicator linearProgressIndicator;
    private TextView tvTitleStep, tvDescription;

    public Step5Fragment(LinearProgressIndicator linearProgressIndicator, TextView tvTitleStep, TextView tvDescription) {
        this.linearProgressIndicator = linearProgressIndicator;
        this.tvTitleStep = tvTitleStep;
        this.tvDescription = tvDescription;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_step5;
    }

    @Override
    protected void loadControlsAndResize(View view) {
        linearProgressIndicator.setProgress(60);
        tvTitleStep.setText(getResources().getString(R.string.step_3_title));
        tvDescription.setText(getResources().getString(R.string.step_3_description));

    }
}