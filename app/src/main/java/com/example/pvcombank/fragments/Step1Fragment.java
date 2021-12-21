package com.example.pvcombank.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pvcombank.R;
import com.example.pvcombank.view.CustomButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class Step1Fragment extends BaseFragment implements View.OnClickListener {

    private LinearProgressIndicator linearProgressIndicator;
    private TextView tvTitleStep, tvDescription;
    private RelativeLayout layoutFontSide, layoutBackside;
    private ImageView imgCheckFrontSide, imgCheckBackside, imgFrontSide, imgBackSide;
    private CustomButton btnTakePhoto1, btnTakePhoto2, btnContinue;
    private LinearLayout layoutTvWarning1, layoutTvWarning2;
    private boolean isFontSide = true;

    public Step1Fragment(LinearProgressIndicator linearProgressIndicator, TextView tvTitleStep, TextView tvDescription) {
        this.linearProgressIndicator = linearProgressIndicator;
        this.tvTitleStep = tvTitleStep;
        this.tvDescription = tvDescription;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_step1;
    }

    @Override
    protected void loadControlsAndResize(View view) {
        linearProgressIndicator.setProgress(0);
        tvTitleStep.setText(getResources().getString(R.string.step_2_title));
        tvDescription.setText(getResources().getString(R.string.step_2_description));
        layoutFontSide = view.findViewById(R.id.layout_frontSide);
        layoutFontSide.getLayoutParams().width = mainActivity.getSizeWithScale(472.5);
        layoutFontSide.getLayoutParams().height = mainActivity.getSizeWithScale(294);
        layoutBackside = view.findViewById(R.id.layout_img_backside);
        layoutBackside.getLayoutParams().width = mainActivity.getSizeWithScale(472.5);
        layoutBackside.getLayoutParams().height = mainActivity.getSizeWithScale(294);
        imgCheckFrontSide = view.findViewById(R.id.img_check_fontSide);
        imgCheckFrontSide.getLayoutParams().width = mainActivity.getSizeWithScale(40);
        imgCheckFrontSide.getLayoutParams().height = mainActivity.getSizeWithScale(40);
        imgCheckBackside = view.findViewById(R.id.img_check_backside);
        imgCheckBackside.getLayoutParams().width = mainActivity.getSizeWithScale(40);
        imgCheckBackside.getLayoutParams().height = mainActivity.getSizeWithScale(40);
        btnTakePhoto1 = view.findViewById(R.id.btn_take_photo_1);
        btnTakePhoto1.getLayoutParams().width = mainActivity.getSizeWithScale(120);
        btnTakePhoto1.getLayoutParams().height = mainActivity.getSizeWithScale(40);
        btnTakePhoto2 = view.findViewById(R.id.btn_take_photo_2);
        btnTakePhoto2.getLayoutParams().width = mainActivity.getSizeWithScale(120);
        btnTakePhoto2.getLayoutParams().height = mainActivity.getSizeWithScale(40);
        layoutTvWarning1 = view.findViewById(R.id.layout_tv_warning_1);
        layoutTvWarning2 = view.findViewById(R.id.layout_tv_warning_2);
        btnTakePhoto1.setOnClickListener(this);
        btnTakePhoto2.setOnClickListener(this);
        btnContinue = view.findViewById(R.id.btn_continue);
        btnContinue.getLayoutParams().width = mainActivity.getSizeWithScale(120);
        btnContinue.getLayoutParams().height = mainActivity.getSizeWithScale(40);
        btnContinue.setOnClickListener(this);
        imgFrontSide = view.findViewById(R.id.img_font_side);
        imgBackSide = view.findViewById(R.id.img_back_side);
        mainActivity.btnTakePhoto.setOnClickListener(this);

    }

    // chụp mặt trước thành công
    private void fontSideSuccess() {
        layoutTvWarning1.setVisibility(View.GONE);
        layoutFontSide.setBackground(getResources().getDrawable(R.drawable.bg_layout_img_success));
        imgCheckFrontSide.setVisibility(View.VISIBLE);
        imgCheckFrontSide.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
    }

    // chụp mặt trước thất bại
    private void fontSideError() {
        layoutTvWarning1.setVisibility(View.VISIBLE);
        layoutFontSide.setBackground(getResources().getDrawable(R.drawable.bg_layout_img_error));
        imgCheckFrontSide.setVisibility(View.VISIBLE);
        imgCheckFrontSide.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel));
    }

    // chụp mặt sau thành công
    private void backsideSuccess() {
        layoutTvWarning2.setVisibility(View.GONE);
        layoutBackside.setBackground(getResources().getDrawable(R.drawable.bg_layout_img_success));
        imgCheckBackside.setVisibility(View.VISIBLE);
        imgCheckBackside.setImageDrawable(getResources().getDrawable(R.drawable.ic_check));
    }

    // chụp mặt sau thất bại
    private void backsideError() {
        layoutTvWarning2.setVisibility(View.VISIBLE);
        layoutBackside.setBackground(getResources().getDrawable(R.drawable.bg_layout_img_error));
        imgCheckBackside.setVisibility(View.VISIBLE);
        imgCheckBackside.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel));
    }

    private void nextScreen() {
        mainActivity.addOrReplaceFragment(R.id.content_steps, new Step2Fragment(linearProgressIndicator, tvTitleStep, tvDescription), true, true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_photo_1:
                isFontSide = true;
                if (isFullPermission() == false)
                    requestPermissions();
                else
                    mainActivity.startCamera();
                break;
            case R.id.btn_take_photo_2:
                isFontSide = false;
                if (isFullPermission() == false)
                    requestPermissions();
                else
                    mainActivity.startCamera();
                break;
            case R.id.btn_continue:
                nextScreen();
                break;
            case R.id.btn_take_photo:
                takePhoto();
                break;
        }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                mainActivity.MY_PERMISSIONS_CODE_1);
    }

    public void takePhoto() {
        mainActivity.layoutTakePhoto.setVisibility(View.GONE);
        File file = new File(mainActivity.getBatchDirectoryName(), "image" + ".jpg");
//        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "image");
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        if (mainActivity.imageCapture == null) {
            return;
        }
        mainActivity.imageCapture.takePicture(outputFileOptions, mainActivity.executor, new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    ExifInterface exif = null;
                    try {
                        exif = new ExifInterface(file.getAbsoluteFile().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    int rotationInDegrees = mainActivity.exifToDegrees(rotation);
                    Matrix matrix = new Matrix();
                    matrix.preRotate(rotationInDegrees);
                    Bitmap myBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                    final Bitmap finalMyBitmap = Bitmap.createBitmap(myBitmap, 190, 920, 2050, 1380);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isFontSide) {
                                imgFrontSide.setImageBitmap(finalMyBitmap);
                                fontSideSuccess();
                            } else {
                                imgBackSide.setImageBitmap(finalMyBitmap);
                                backsideError();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(@NonNull ImageCaptureException error) {
                error.printStackTrace();
            }
        });
    }

    private boolean isFullPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else
            return false;
    }
}