package com.example.books4share;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.books4share.fragment.HomeFragment;
import com.example.books4share.fragment.NotificationFragment;
import com.example.books4share.fragment.ProfileFragment;
import com.example.books4share.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Initiate the home page with add book function and 4 bottom navigation buttons
 */
public class MainActivity extends AppCompatActivity {
    FrameLayout container;
    BottomNavigationView mBottomNavigationBar;
    private static final String FRAGMENT_TAG_APP = "fragment_app";
    private static final String FRAGMENT_TAG_DISCOVER = "fragment_discover";
    private static final String FRAGMENT_TAG_NOTIFICATION= "fragment_notification";
    private static final String FRAGMENT_TAG_MINE = "fragment_profile";
    private String[] mFragmentTags = new String[]{FRAGMENT_TAG_APP, FRAGMENT_TAG_DISCOVER,FRAGMENT_TAG_NOTIFICATION,FRAGMENT_TAG_MINE};
    private Fragment mAppFragment;
    private Fragment mMineFragment;
    private SearchFragment mSearchFragment;
    private NotificationFragment mNotificationFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * initialize the activity view
     */
    protected void initView() {
        mBottomNavigationBar = findViewById(R.id.mBottomNavigationView);
        container = findViewById(R.id.container);
        mBottomNavigationBar.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ontBottomSelect(FRAGMENT_TAG_APP);
                    break;
                case R.id.navigation_explore:
                    ontBottomSelect(FRAGMENT_TAG_DISCOVER);
                    break;
                case R.id.navigation_notification:
                    ontBottomSelect(FRAGMENT_TAG_NOTIFICATION);
                    break;
                case R.id.navigation_Me:
                    ontBottomSelect(FRAGMENT_TAG_MINE);
                    break;

                default:
                    break;
            }
            return true;
        });

        mBottomNavigationBar.setSelectedItemId(mBottomNavigationBar.getMenu().getItem(0).getItemId());

    }


    /**
     * NavigationBar listener
     * @param tag
     */
    private void ontBottomSelect(String tag) {
        switch (tag) {

            case FRAGMENT_TAG_APP:
                if (mAppFragment == null) {
                    mAppFragment = HomeFragment.newInstance();
                }
                switchFragment(mAppFragment, tag);
                break;
            case FRAGMENT_TAG_DISCOVER:
                if (mSearchFragment == null) {
                    mSearchFragment = SearchFragment.newInstance();
                }
                switchFragment(mSearchFragment, tag);
                break;
            case FRAGMENT_TAG_NOTIFICATION:
                if (mNotificationFragment == null) {
                    mNotificationFragment = NotificationFragment.newInstance();
                }
                switchFragment(mNotificationFragment, tag);
                break;
            case FRAGMENT_TAG_MINE:
                if (mMineFragment == null) {
                    mMineFragment = ProfileFragment.newInstance();
                }
                switchFragment(mMineFragment, tag);
                break;
            default:
                break;
        }
    }
    private void switchFragment(Fragment fragment, String tag) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(manager, transaction);
        if (!fragment.isAdded()) {
            transaction.add(R.id.container, fragment, tag);
        }
        transaction.show(fragment).commit();
    }


    private void hideFragments(FragmentManager fragmentManager, FragmentTransaction transaction) {
        for (String mFragmentTag : mFragmentTags) {
            Fragment fragment = fragmentManager.findFragmentByTag(mFragmentTag);
            if (fragment != null && fragment.isVisible()) {
                transaction.hide(fragment);
            }
        }
    }
}
