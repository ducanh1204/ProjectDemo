package com.example.pvcombank.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.pvcombank.R;
import com.example.pvcombank.view.CustomButton;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class Step2Fragment extends BaseFragment implements View.OnClickListener {
    private LinearProgressIndicator linearProgressIndicator;
    private TextView tvTitleStep, tvDescription;
    private ImageView imgTutorial;
    private RelativeLayout layoutImgTutorial;
    private CustomButton btnContinue;

    public Step2Fragment(LinearProgressIndicator linearProgressIndicator, TextView tvTitleStep, TextView tvDescription) {
        this.linearProgressIndicator = linearProgressIndicator;
        this.tvTitleStep = tvTitleStep;
        this.tvDescription = tvDescription;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_step2;
    }

    @Override
    protected void loadControlsAndResize(View view) {
        linearProgressIndicator.setProgress(20);
        tvTitleStep.setText(getResources().getString(R.string.step_2_title));
        tvDescription.setText(getResources().getString(R.string.step_2_description));
        layoutImgTutorial = view.findViewById(R.id.layout_img_tutorial);
        layoutImgTutorial.getLayoutParams().width = mainActivity.getSizeWithScale(472.5);
        layoutImgTutorial.getLayoutParams().height = mainActivity.getSizeWithScale(294);
        imgTutorial = view.findViewById(R.id.img_tutorial);
        imgTutorial.getLayoutParams().width = mainActivity.getSizeWithScale(135);
        imgTutorial.getLayoutParams().height = mainActivity.getSizeWithScale(179);
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.getLayoutParams().width = mainActivity.getSizeWithScale(120);
        btnContinue.getLayoutParams().height = mainActivity.getSizeWithScale(40);
        btnContinue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                checkInfoPagier(v);
                break;
        }
    }

    private void checkInfoPagier(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        View alertView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_warning_info_pagier_invalid, v.findViewById(R.id.layout_dialog));

        RelativeLayout relativeLayout = alertView.findViewById(R.id.layout_dialog);
        relativeLayout.getLayoutParams().width = mainActivity.getSizeWithScale(500);
        relativeLayout.getLayoutParams().height = mainActivity.getSizeWithScale(256);


        ImageView imgIconInfo = alertView.findViewById(R.id.img_ic_error);
        imgIconInfo.getLayoutParams().width = mainActivity.getSizeWithScale(30);
        imgIconInfo.getLayoutParams().height = mainActivity.getSizeWithScale(30);

        CustomButton btnAgree = alertView.findViewById(R.id.btn_agree);
        btnAgree.getLayoutParams().width = mainActivity.getSizeWithScale(137);
        btnAgree.getLayoutParams().height = mainActivity.getSizeWithScale(46);

        alert.setView(alertView);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                nextScreen();
            }
        });
        dialog.show();
    }

    private void nextScreen(){
        mainActivity.addOrReplaceFragment(R.id.content_steps,new Step3Fragment(linearProgressIndicator,tvTitleStep,tvDescription),true,true);
    }
}