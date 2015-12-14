package com.placego;


import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.placego.util.ItemUser;
import com.placego.util.PersonLocation;

/**
 * Created by juanes on 14/11/15.
 */
public class ApplicationLoader extends Application {

    public static volatile Context applicationContext;
    private final static String KEY_ID = "JBY83Ak7IRPRx5IfauRY1SdqQmj5gJL9ypKDnFUF";
    private final static String KEY_CLIENTE = "2pcE45Ng5oHXBzvObqjcPMsg6Y1LR9VPtggqb22l";

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = getApplicationContext();

        ParseObject.registerSubclass(PersonLocation.class);
        Parse.initialize(this, KEY_ID, KEY_CLIENTE);

        ParseUser.enableAutomaticUser();
        final ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }

}
