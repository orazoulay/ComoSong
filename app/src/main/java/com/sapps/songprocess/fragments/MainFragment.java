package com.sapps.songprocess.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sapps.songprocess.R;

import org.w3c.dom.Text;

public class MainFragment extends ComoSongFragment {

private Button btnStartProcess;
private RecyclerView rvOpenProcesses;
private TextView tvTitle;

private OnStartProcessClickListener onStartProcessClickListener;


    @Override
    protected int initLayout() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        btnStartProcess = view.findViewById(R.id.btnStartProcess);
        rvOpenProcesses = view.findViewById(R.id.rvOpenProcesses);
        tvTitle = view.findViewById(R.id.tvTitle);
    }

    @Override
    protected void initTexts() {
        super.initTexts();
        tvTitle.setText("ברוך הבא "+app().getUserAccountManager().getUser().getName());
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        btnStartProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onStartProcessClickListener!=null){
                    onStartProcessClickListener.onStartProcess();
                }

            }
        });
    }

    public void setOnStartProcessClickListener(OnStartProcessClickListener onStartProcessClickListener) {
        this.onStartProcessClickListener = onStartProcessClickListener;
    }

    public interface OnStartProcessClickListener{
        void onStartProcess();
    }
}
