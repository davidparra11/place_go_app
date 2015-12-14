package com.placego;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseUser;
import com.placego.fragments.MainInicio;
import com.placego.fragments.MainLogin;
import com.placego.fragments.MainRegister;
import com.placego.ui.Home;
import com.placego.ui.widget.CirclePageIndicator;
import com.placego.ui.widget.ParallaxViewPager;
import com.placego.util.StringUtil;
import com.placego.util.listener.MainOnLoadingListener;

import java.util.Map;

public class Main extends AppCompatActivity implements MainOnLoadingListener {

    private final static int MAX_PAGES = 3;
    private ProgressDialog progressDialog;
    private boolean destroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(StringUtil.KEY_PREFERENCES_DATA, MODE_PRIVATE);
        final Map<String, ?> state = preferences.getAll();
        if (!state.isEmpty() && ParseUser.getCurrentUser() != null) {
            startActivity(new Intent(this, Home.class));
            super.onCreate(savedInstanceState);
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        final ParallaxViewPager mViewPager = (ParallaxViewPager) findViewById(R.id.pager);
        mViewPager.setMaxPages(MAX_PAGES);
        mViewPager.setBackgroundAsset(R.mipmap.background);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(1);
        mViewPager.setOffscreenPageLimit(2);
        final CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mViewPager);
    }

    @Override
    public void onSignUpSuccess(ParseUser user) {
        savedPreferencesData(user);
        onLoadingFinish();
        startActivity(new Intent(this, Home.class));
        finish();
    }

    private void savedPreferencesData(ParseUser user) {
        final SharedPreferences preferences = ApplicationLoader.applicationContext.getSharedPreferences(StringUtil.KEY_PREFERENCES_DATA, MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString(StringUtil.KEY_PREFERENCES_DATA_USER, user.getUsername());
        editor.putString(StringUtil.KEY_PREFERENCES_DATA_EMAIL, user.getEmail());
        editor.putString(StringUtil.KEY_PREFERENCES_DATA_USER, user.getObjectId());
        editor.apply();
    }

    @Override
    public void onLoginSuccess(ParseUser user) {
        savedPreferencesData(user);
        onLoadingFinish();
        startActivity(new Intent(this, Home.class));
        finish();
    }

    @Override
    public void onLoadingStart(String text) {
        progressDialog = ProgressDialog.show(this, null, text, true);
    }

    @Override
    public void onLoadingFinish() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        }
        return destroyed;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        destroyed = true;
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = new MainRegister();
                    break;
                case 1:
                    fragment = new MainInicio();
                    break;
                case 2:
                    fragment = new MainLogin();
                    break;
                default:
                    fragment = new MainRegister();
                    break;

            }
            return fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGES;
        }
    }

}
