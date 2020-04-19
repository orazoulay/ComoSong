package com.sapps.songprocess.managers;

import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sapps.songprocess.Application;
import com.sapps.songprocess.R;
import com.sapps.songprocess.activities.MainActivity;
import com.sapps.songprocess.data.OpenSong;
import com.sapps.songprocess.data.User;
import com.sapps.songprocess.fragments.ChooseSongProperties;
import com.sapps.songprocess.fragments.LoginFragment;
import com.sapps.songprocess.fragments.MainFragment;
import com.sapps.songprocess.fragments.SongFragment;
import com.sapps.songprocess.requests.AuthenticationRequests;

import java.util.List;


public class UserAccountManager extends Manager {

    public MainActivity.OnVideoReturnListener videoReturnListener;
    User user;

    private Application app() {
        return Application.getApp();
    }


    public static UserAccountManager userAccountManager;

    protected UserAccountManager(Application application) {
        super(application);
    }

    public static UserAccountManager getInstance(Application application) {
        if (userAccountManager == null) {
            userAccountManager = new UserAccountManager(application);
        }
        return userAccountManager;
    }


    @Override
    public void init() {
        super.init();
        user = null;
    }


    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void pushLoginFragment() {
        Fragment fragment = new LoginFragment();
        pushFragment(fragment);

    }

    public void pushSongFragment(OpenSong song) {
        SongFragment fragment = new SongFragment(song);
        fragment.setOnContinueProcess(new SongFragment.OnContinueProcess() {
            @Override
            public void onContinue(MainActivity.OnVideoReturnListener onVideoReturnListener) {
                videoReturnListener = onVideoReturnListener;
//                popFragments();
                app().getCurrectActivity().setCameraActivity();
            }

            @Override
            public void onSendToServer(Uri uri) {
                AuthenticationRequests.sendUploadVideoRequest(app(),app().getUserAccountManager().getUser().getUid(),uri);

            }
        });
        pushFragment(fragment);

    }

    private void popFragments() {
        FragmentManager fm = app().getCurrectActivity().getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    public void pushChooseSongPropertiesFragment(List<User> users) {
        ChooseSongProperties fragment = new ChooseSongProperties(users);
        fragment.setOnChooseSongListener(new ChooseSongProperties.OnChooseSongListener() {
            @Override
            public void onContinue(List<User> users, String song) {
                AuthenticationRequests.sendUpdateUserRequest(app(), users, song);
            }
        });

        pushFragment(fragment);

    }

    public void pushMainFragment() {
        MainFragment fragment = new MainFragment();

        fragment.setOnStartProcessClickListener(new MainFragment.OnStartProcessClickListener() {
            @Override
            public void onStartProcess() {
//                pushChooseSongPropertiesFragment();
                AuthenticationRequests.sendGetUsersRequest(app());
            }

            @Override
            public void onStartExistProject(OpenSong song) {
                pushSongFragment(song);
            }
        });

        pushFragment(fragment);
    }

    private void pushFragment(Fragment fragment) {
        FragmentTransaction transaction = app().getCurrectActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.flContainer, fragment);
        if(!fragment.equals(MainFragment.class)){
            transaction.addToBackStack(fragment.getTag());
        }
        transaction.commit();
    }


}
