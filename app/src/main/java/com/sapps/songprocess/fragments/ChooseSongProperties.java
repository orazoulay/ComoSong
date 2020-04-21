package com.sapps.songprocess.fragments;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sapps.songprocess.R;
import com.sapps.songprocess.adapters.SongsAdapter;
import com.sapps.songprocess.adapters.UsersAdapter;
import com.sapps.songprocess.data.User;

import java.util.ArrayList;
import java.util.List;

public class ChooseSongProperties extends ComoSongFragment implements UsersAdapter.ItemClickListener, SongsAdapter.ItemClickListener {
    public static final int SONG = 1;
    public static final int USER = 2;
    private RecyclerView rvSongs;
    private RecyclerView rvFriends;
    private TextView tvFirendsCounter;
    private TextView tvSongChoosen;
    private OnChooseSongListener onChooseSongListener;
    private Button btnContinue;


    private List<User> users;
    private List<String> songs;
    private String choosenSong;
    private List<User> choosenUsers;
    private String ChoosenFriends = "חברים שנבחרו: ";
    private String Friends = "מספר חברים : ";


    public ChooseSongProperties(List<User> users) {
        super();
        if (users != null) {
            this.users = new ArrayList(users);
            this.songs = new ArrayList<>();
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
        tvFirendsCounter = view.findViewById(R.id.tvFriendsCounter);
        tvSongChoosen = view.findViewById(R.id.tvSongChoosen);
        choosenUsers = new ArrayList<>();

    }



    @Override
    protected void initTexts() {
        super.initTexts();
        tvFirendsCounter.setText(Friends + users.size());
        tvSongChoosen.setText("שיר שנבחר: ");

    }


    @Override
    protected void initListeners() {
        super.initListeners();
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onChooseSongListener != null) {
                    onChooseSongListener.onContinue(choosenUsers, choosenSong);
                }
            }
        });
    }

    @Override
    protected void initAdapters() {
        super.initAdapters();
        songs.add("שבט אחים ואחיות - עידן ריייכל");
        songs.add("Imagine - John Lennon");
        SongsAdapter songsAdapter = new SongsAdapter(app(), songs);
        users.remove(app().getUserAccountManager().getUser());
        UsersAdapter usersAdapter = new UsersAdapter(app(), users);
        rvFriends.setLayoutManager(new LinearLayoutManager(app()));
        rvSongs.setLayoutManager(new LinearLayoutManager(app()));
        usersAdapter.setClickListener(this);
        songsAdapter.setClickListener(this);
        rvSongs.setAdapter(songsAdapter);
        rvFriends.setAdapter(usersAdapter);
    }

    public void setOnChooseSongListener(OnChooseSongListener onChooseSongListener) {
        this.onChooseSongListener = onChooseSongListener;
    }


    @Override
    public void onItemClick(View view, int position, int type) {
        switch (type) {
            case SONG:
                choosenSong = songs.get(position);
                tvSongChoosen.setText("שיר שנבחר: " + songs.get(position));

                break;

            case USER:
                if (choosenUsers.contains(users.get(position))) {
                    choosenUsers.remove(users.get(position));
                    view.setSelected(false);

                    tvFirendsCounter.setText(Friends + users.size() + "  " + ChoosenFriends + choosenUsers.size());

                } else {
                    choosenUsers.add(users.get(position));
                    view.setSelected(true);
                    tvFirendsCounter.setText(Friends + users.size() + "  " + ChoosenFriends + choosenUsers.size());


                }
                break;
        }


    }

    public interface OnChooseSongListener {
        void onContinue(List<User> users, String song);
    }


}
