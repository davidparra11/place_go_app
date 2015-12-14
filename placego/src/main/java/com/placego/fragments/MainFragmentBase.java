package com.placego.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.placego.Main;
import com.placego.util.listener.MainOnLoadingListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by juanes on 19/11/15.
 */
public class MainFragmentBase extends Fragment {

    protected MainOnLoadingListener onLoadingListener;
    protected static final String EMAIL_PATTERN = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    protected static final String USER_PATTERN = "^[a-z0-9_-]{3,15}$";
    protected Pattern pattern;
    protected Matcher matcher;

    protected void showToast(int id) {
        showToast(getString(id));
    }

    protected void showToast(CharSequence text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    protected void loadingStart(String message) {
        if (onLoadingListener != null) {
            onLoadingListener.onLoadingStart(message);
        }
    }

    protected void loadingFinish() {
        if (onLoadingListener != null) {
            onLoadingListener.onLoadingFinish();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    protected boolean isActivityDestroyed() {
        FragmentActivity activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return activity == null || activity.isDestroyed();
        } else {
            return activity == null || ((Main) activity).isDestroyed();
        }
    }

    protected boolean validateEmail(String email) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    protected boolean validateUser(String user) {
        pattern = Pattern.compile(USER_PATTERN);
        matcher = pattern.matcher(user);
        return matcher.matches();
    }

    protected void hideKeyboard() {
        final View view = getActivity().getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
