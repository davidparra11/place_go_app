package com.placego.util.listener;

import com.parse.ParseUser;

/**
 * Created by juanes on 19/11/15.
 */
public interface MainOnLoadingListener {
    void onLoadingStart(String text);

    void onLoadingFinish();

    void onLoginSuccess(ParseUser user);

    void onSignUpSuccess(ParseUser user);
}
