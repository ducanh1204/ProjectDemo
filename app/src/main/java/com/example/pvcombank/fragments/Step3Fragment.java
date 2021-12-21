package com.example.pvcombank.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pvcombank.R;
import com.example.pvcombank.view.CustomButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class Step3Fragment extends BaseFragment implements View.OnClickListener {
    private LinearProgressIndicator linearProgressIndicator;
    private TextView tvTitleStep, tvDescription;
    private ImageView img1, img2;
    private CustomButton btnStartTakePortrait;

    public Step3Fragment(LinearProgressIndicator linearProgressIndicator, TextView tvTitleStep, TextView tvDescription) {
        this.linearProgressIndicator = linearProgressIndicator;
        this.tvTitleStep = tvTitleStep;
        this.tvDescription = tvDescription;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_step3;
    }

    @Override
    protected void loadControlsAndResize(View view) {
        linearProgressIndicator.setProgress(40);
        tvTitleStep.setText(getResources().getString(R.string.step_3_title));
        tvDescription.setText(getResources().getString(R.string.step_3_description));
        img1 = view.findViewById(R.id.img_1);
        img1.getLayoutParams().width = mainActivity.getSizeWithScale(280);
        img1.getLayoutParams().height = mainActivity.getSizeWithScale(205);
        img2 = view.findViewById(R.id.img_2);
        img2.getLayoutParams().width = mainActivity.getSizeWithScale(44);
        img2.getLayoutParams().height = mainActivity.getSizeWithScale(44);
        btnStartTakePortrait = view.findViewById(R.id.btn_start_take_portraits);
        btnStartTakePortrait.getLayoutParams().width = mainActivity.getSizeWithScale(355);
        btnStartTakePortrait.getLayoutParams().height = mainActivity.getSizeWithScale(40);
        btnStartTakePortrait.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_take_portraits:
                nextScreen();
                break;
        }
    }
    private void nextScreen(){
        mainActivity.addOrReplaceFragment(R.id.content_steps,new Step5Fragment(linearProgressIndicator,tvTitleStep,tvDescription),true,true);
    }
}