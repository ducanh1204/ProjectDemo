package com.example.pvcombank.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.extensions.HdrImageCaptureExtender;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.pvcombank.R;
import com.example.pvcombank.adapters.ViewPagerAdapter;
import com.example.pvcombank.fragments.Tab1Fragment;
import com.example.pvcombank.view.CustomButton;
import com.example.pvcombank.view.CustomImageButton;
import com.example.pvcombank.view.CustomTabLayout;
import com.example.pvcombank.view.CustomViewPager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private AppBarLayout appBarLayout;
    private CustomTabLayout tabLayout;
    public CustomViewPager viewPager;
    private Toolbar toolbar;
    private ImageView imgLogoAstecPVCombank;
    private long backPressedTime;
    private ViewPagerAdapter viewPagerAdapter;
    private PreviewView previewView;
    public CustomButton btnTakePhoto;
    private CustomImageButton btnClose;
    public RelativeLayout layoutTakePhoto;
    private View viewCamera;
    public final int MY_PERMISSIONS_CODE_1 = 101;
    public Executor executor = Executors.newSingleThreadExecutor();
    public ImageCapture imageCapture;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    public void loadControlsAndResize() {
        appBarLayout = findViewById(R.id.app_bar_layout);
        appBarLayout.getLayoutParams().height = getSizeWithScale(55);
        toolbar = findViewById(R.id.toolbar);
        initDialogLoading();
        setSupportActionBar(toolbar);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), ViewPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.addFrag(new Tab1Fragment(), getResources().getString(R.string.tab_layout_1_title));
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_mail);
        imgLogoAstecPVCombank = findViewById(R.id.img_logo_astec_pvcombank);
        imgLogoAstecPVCombank.getLayoutParams().width = getSizeWithScale(140);
        imgLogoAstecPVCombank.getLayoutParams().height = getSizeWithScale(20);
        previewView = findViewById(R.id.preview_view);
        btnTakePhoto = findViewById(R.id.btn_take_photo);
        btnTakePhoto.getLayoutParams().width = getSizeWithScale(100);
        btnTakePhoto.getLayoutParams().height = getSizeWithScale(100);
        btnClose = findViewById(R.id.btn_close);
        btnClose.getLayoutParams().width = getSizeWithScale(60);
        btnClose.getLayoutParams().height = getSizeWithScale(60);
        btnClose.setOnClickListener(this);
        layoutTakePhoto = findViewById(R.id.layout_take_photo);
        viewCamera = findViewById(R.id.view_camera);
        viewCamera.getLayoutParams().width = getSizeWithScale(765);
        viewCamera.getLayoutParams().height = getSizeWithScale(486);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_CODE_1:
                int countGranted = 0;
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        countGranted++;
                    }
                }
                if (countGranted == grantResults.length) {
                    startCamera();
                } else {
                    showToast("Yêu cầu cấp quyền cho ứng dụng");
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (layoutTakePhoto.getVisibility() == View.VISIBLE) {
            layoutTakePhoto.setVisibility(View.GONE);
        } else {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (count == 0) {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    super.onBackPressed();
                    finishAffinity();
                    return;
                } else {
                    showToast(getResources().getString(R.string.toast_backpress));
                }
                backPressedTime = System.currentTimeMillis();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                layoutTakePhoto.setVisibility(View.GONE);
                break;
        }
    }

    public void startCamera() {
        layoutTakePhoto.setVisibility(View.VISIBLE);
        final ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                    cameraProvider.unbindAll();
                    bindPreview(cameraProvider);

                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder()
                .build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .build();
        ImageCapture.Builder builder = new ImageCapture.Builder();
        HdrImageCaptureExtender hdrImageCaptureExtender = HdrImageCaptureExtender.create(builder);
        if (hdrImageCaptureExtender.isExtensionAvailable(cameraSelector)) {
            hdrImageCaptureExtender.enableExtension(cameraSelector);
        }
        imageCapture = builder
                .setTargetRotation(this.getWindowManager().getDefaultDisplay().getRotation())
                .build();
        preview.setSurfaceProvider(this.previewView.createSurfaceProvider());
        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis, imageCapture);
    }

    public String getBatchDirectoryName() {
        String app_folder_path = "";
        app_folder_path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        return app_folder_path;
    }

    public int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
}
