package com.example.pvcombank.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pvcombank.activities.MainActivity;

public abstract class BaseFragment extends Fragment {
    protected abstract int getLayoutResId();
    protected abstract void loadControlsAndResize(View view);
    protected MainActivity mainActivity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), container, false);
        if(mainActivity == null)
            mainActivity = (MainActivity) getActivity();
        loadControlsAndResize(view);
        return view;
    }
}
