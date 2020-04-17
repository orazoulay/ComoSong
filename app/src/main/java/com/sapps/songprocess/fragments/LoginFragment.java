package com.sapps.songprocess.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sapps.songprocess.Application;
import com.sapps.songprocess.R;
import com.sapps.songprocess.activities.MainActivity;
import com.sapps.songprocess.requests.AuthenticationRequests;
import com.sapps.songprocess.requests.BaseRequest;


public class LoginFragment extends ComoSongFragment {
    private TextInputLayout tiUserName,tiPassword;
    private TextInputEditText etUserName,etPassword;
    private Button btnContinue;



    @Override
    protected int initLayout() {
        return R.layout.fragment_login;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        tiPassword = view.findViewById(R.id.tiPassword);
        tiUserName = view.findViewById(R.id.tiUserName);
        etUserName = view.findViewById(R.id.etUserName);
        etPassword = view.findViewById(R.id.etPassword);
        btnContinue = view.findViewById(R.id.btnContinue);

    }

    @Override
    protected void initTexts() {
        super.initTexts();
        tiUserName.setHint("שם משתמש:");
        tiPassword.setHint("סיסמה:");
        etUserName.setText("user1");
        etPassword.setText("123456");
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationRequests.sendLoginRequest(getActivity(),etUserName.getText().toString(),etPassword.getText().toString());
            }
        });
    }




}
