package com.sapps.songprocess.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.sapps.songprocess.Application;

public class ComoSongFragment extends Fragment {

    protected Application app() {
        return Application.getApp();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initLayout(), container, false);
        initViews(view);
        initTexts();
        initListeners();
        initAdapters();
        return view;
    }

    protected int initLayout() {
        return 0;
    }

    protected void initViews(View view) {
    }

    protected void initTexts() {
    }

    protected void initListeners() {

    }
    protected void initAdapters() {

    }
}
