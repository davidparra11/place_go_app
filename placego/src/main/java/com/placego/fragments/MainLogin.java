package com.placego.fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.placego.Main;
import com.placego.R;
import com.placego.ui.widget.PaperButton;

public class MainLogin extends MainFragmentBase implements View.OnClickListener {


    public MainLogin() {
    }

    private EditText mainUser, mainPassword;
    private String username, password;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_login, container, false);

        mainUser = (EditText) view.findViewById(R.id.login_text_user);
        mainPassword = (EditText) view.findViewById(R.id.login_text_pass);
        mainPassword.setTypeface(Typeface.DEFAULT);
        mainPassword.setTransformationMethod(new PasswordTransformationMethod());

        final PaperButton registerButton = (PaperButton) view.findViewById(R.id.login_enter);
        registerButton.setOnClickListener(this);

        mainPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    onClick(registerButton);
                    return true;
                }
                return false;
            }
        });
        final TextView forgot = (TextView) view.findViewById(R.id.login_forgot);
        forgot.setText(Html.fromHtml(getString(R.string.login_forgot)));
        forgot.setMovementMethod(LinkMovementMethod.getInstance());


        return view;
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        username = mainUser.getText().toString().trim().toLowerCase();
        password = mainPassword.getText().toString().trim();

        if (!username.isEmpty()) {
            if (!validateUser(username)) {
                showToast(R.string.register_user_invalido);
                return;
            }
        } else {
            showToast(R.string.register_user_blank);
            return;
        }

        if (password.isEmpty()) {
            showToast(R.string.register_pass_blank);
            return;
        } else if (password.length() <= 5) {
            showToast(R.string.main_pass_minimum);
            return;
        } else {
            loadingStart(getString(R.string.register_message));
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (isActivityDestroyed()) {
                        return;
                    }
                    if (user != null) {
                        onLoadingListener.onLoginSuccess(user);
                    } else {
                        loadingFinish();
                        if (e != null) {
                            if (e.getCode() == ParseException.OBJECT_NOT_FOUND)
                                showToast(R.string.main_error_login);
                            else
                                showToast(R.string.main_error_unknown);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Main) {
            onLoadingListener = (Main) context;
        }
    }
}
