package com.sapps.songprocess.fragments;

import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sapps.songprocess.R;
import com.sapps.songprocess.adapters.UsersAdapter;
import com.sapps.songprocess.data.User;

import java.util.ArrayList;
import java.util.List;

public class ChooseSongProperties extends ComoSongFragment  implements UsersAdapter.ItemClickListener  {
    private RecyclerView  rvSongs;
    private RecyclerView  rvFriends;
    private Button btnContinue;
    private List<User> users;
    private OnChooseSongListener onChooseSongListener;
    private List<User> choosenUsers;

    public ChooseSongProperties(List<User> users) {
        super();
        if(users!=null) {
            this.users = new ArrayList(users);
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_choose_song_properties;
    }

    @Override
    protected void initViews(View view) {
        super.initViews(view);
        rvSongs = view.findViewById(R.id.rvSongs);
        rvFriends = view.findViewById(R.id.rvFriends);
        btnContinue = view.findViewById(R.id.btnContinue);
        choosenUsers = new ArrayList<>();
    }

    @Override
    protected void initTexts() {
        super.initTexts();
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onChooseSongListener!=null){
                    onChooseSongListener.onContinue();
                }
            }
        });
    }

    @Override
    protected void initAdapters() {
        super.initAdapters();
        UsersAdapter usersAdapter = new UsersAdapter(app(),users);
        rvFriends.setLayoutManager(new LinearLayoutManager(app()));
        usersAdapter.setClickListener(this);
        rvFriends.setAdapter(usersAdapter);
    }

    public void setOnChooseSongListener(OnChooseSongListener onChooseSongListener) {
        this.onChooseSongListener = onChooseSongListener;
    }

    @Override
    public void onItemClick(View view, int position) {
        if(choosenUsers.contains(users.get(position))){
            choosenUsers.remove(users.get(position));
        }
        else {
            choosenUsers.add(users.get(position));
        }

    }

    public interface OnChooseSongListener{
        void onContinue();
    }

}
