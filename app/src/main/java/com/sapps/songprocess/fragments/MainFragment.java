package com.sapps.songprocess.fragments;

import android.graphics.Path;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sapps.songprocess.R;
import com.sapps.songprocess.adapters.OpenSongsAdapter;
import com.sapps.songprocess.adapters.UsersAdapter;
import com.sapps.songprocess.data.OpenSong;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends ComoSongFragment implements OpenSongsAdapter.ItemClickListener {
    public static final int OPENSONG = 3;
    private Button btnStartProcess;
    private Button btnContinue;
    private RecyclerView rvOpenProcesses;
    private TextView tvTitle;
    private List<OpenSong> openSongs = new ArrayList<>();
    private OnStartProcessClickListener onStartProcessClickListener;
    private OpenSong openSong;


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
        btnContinue = view.findViewById(R.id.btnContinue);
    }

    @Override
    protected void initTexts() {
        super.initTexts();
        tvTitle.setText("ברוך הבא " + app().getUserAccountManager().getUser().getName());
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        btnStartProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStartProcessClickListener != null) {
                    onStartProcessClickListener.onStartProcess();
                }

            }
        });
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onStartProcessClickListener != null && openSong != null) {
                    onStartProcessClickListener.onStartExistProject(openSong);
                }
            }
        });
    }

    @Override
    protected void initAdapters() {
        super.initAdapters();
        openSongs.clear();

        if (app().getUserAccountManager().getUser().getSendSongId().equals(app().getUserAccountManager().getUser().getUid())) {
            app().getUserAccountManager().getUser().setSendSongName(app().getUserAccountManager().getUser().getName());
        }
        if (!app().getUserAccountManager().getUser().getSendSongName().isEmpty()
                && !app().getUserAccountManager().getUser().getOpenSong().isEmpty()) {
            openSongs.add(new OpenSong(app().getUserAccountManager().getUser().getOpenSong()
                    , app().getUserAccountManager().getUser().getSendSongName()));

            OpenSongsAdapter openSongsAdapter = new OpenSongsAdapter(app(), openSongs);
            rvOpenProcesses.setLayoutManager(new LinearLayoutManager(app()));
            openSongsAdapter.setClickListener(this);
            rvOpenProcesses.setAdapter(openSongsAdapter);

        }
    }

    public void setOnStartProcessClickListener(OnStartProcessClickListener onStartProcessClickListener) {
        this.onStartProcessClickListener = onStartProcessClickListener;
    }

    @Override
    public void onItemClick(View view, int position, int type) {
        openSong = openSongs.get(position);
    }

    public interface OnStartProcessClickListener {
        void onStartProcess();

        void onStartExistProject(OpenSong song);
    }
}
