package com.sapps.songprocess.managers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sapps.songprocess.Application;
import com.sapps.songprocess.R;
import com.sapps.songprocess.data.User;
import com.sapps.songprocess.fragments.ChooseSongProperties;
import com.sapps.songprocess.fragments.LoginFragment;
import com.sapps.songprocess.fragments.MainFragment;
import com.sapps.songprocess.fragments.SongFragment;
import com.sapps.songprocess.requests.AuthenticationRequests;

import java.util.List;

public class UserAccountManager extends Manager {

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

    public void pushSongFragment() {
        Fragment fragment = new SongFragment();
        pushFragment(fragment);

    }

    public void pushChooseSongPropertiesFragment(List<User> users) {
        ChooseSongProperties fragment = new ChooseSongProperties(users);
        fragment.setOnChooseSongListener(new ChooseSongProperties.OnChooseSongListener() {
            @Override
            public void onContinue() {

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
        });

        pushFragment(fragment);
    }

    private void pushFragment(Fragment fragment) {
        FragmentTransaction transaction = app().getCurrectActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);
        transaction.replace(R.id.flContainer, fragment);
        transaction.commit();
    }


}
