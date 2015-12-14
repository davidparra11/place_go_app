package com.placego.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.placego.Main;
import com.placego.R;
import com.placego.ui.widget.PaperButton;

public class MainRegister extends MainFragmentBase implements OnClickListener {

    private EditText registerUser, registerEmail, registerPassword;
    private String email, username, password;
    private TextInputLayout emailWrapper;

    public MainRegister() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_register, container, false);
        emailWrapper = (TextInputLayout) view.findViewById(R.id.register_email);
        registerUser = (EditText) view.findViewById(R.id.register_edit_user);
        registerPassword = (EditText) view.findViewById(R.id.register_edit_pass);
        registerEmail = (EditText) view.findViewById(R.id.register_edit_email);
        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateEmail(s.toString()))
                    emailWrapper.setErrorEnabled(false);
                else {
                    if (!emailWrapper.isErrorEnabled())
                        emailWrapper.setError(getString(R.string.register_email_invalido));
                }
            }
        });
        final PaperButton registerButton = (PaperButton) view.findViewById(R.id.register_enter);
        registerButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        hideKeyboard();
        username = registerUser.getText().toString().trim().toLowerCase();
        email = registerEmail.getText().toString().trim();
        password = registerPassword.getText().toString().trim();

        if (!validateEmail(email)) {
            emailWrapper.setError(getString(R.string.register_email_invalido));
            return;
        }

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
        } else if (password.length() <= 5) {
            showToast(R.string.register_pass_minimum);
        } else if (email.isEmpty()) {
            showToast(R.string.register_email_blank);
        } else {
            loadingStart(getString(R.string.register_message));
            final ParseUser user = new ParseUser();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (isActivityDestroyed()) {
                        return;
                    }
                    if (e == null) {
                        loadingFinish();
                        onLoadingListener.onSignUpSuccess(ParseUser.getCurrentUser());
                    } else {
                        loadingFinish();
                        switch (e.getCode()) {
                            case ParseException.INVALID_EMAIL_ADDRESS:
                                showToast(R.string.register_email_invalido);
                                break;
                            case ParseException.USERNAME_TAKEN:
                                showToast(R.string.register_user_ya_existe);
                                break;
                            case ParseException.EMAIL_TAKEN:
                                showToast(R.string.register_email_ya_existe);
                                break;
                            default:
                                showToast(R.string.register_unlogin);
                                break;
                        }
                    }
                }
            });
        }
    }

    /*public void LogIn() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("People");
        query.include("city");
        query.include("city.country");
        query.whereEqualTo("Name", "Mario Rossi");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject person, ParseException e) {
                if (e == null) {
                    ParseObject city = object.get("city");
                    ParseObject country = city.get("country");
                    String continent = country.get("continent");
                } else {
                    //Log.d("person", "Error: " + e.getMessage());
                }
            }
        });*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Main) {
            onLoadingListener = (Main) context;
        }
    }

}
