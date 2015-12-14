package com.placego.util;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by juanes on 23/11/15.
 */
public class StringUtil {

    //DATA PREFERENCES USER
    public static final String KEY_PREFERENCES_DATA = "data_user";
    public static final String KEY_PREFERENCES_DATA_USER = "data_username";
    public static final String KEY_PREFERENCES_DATA_EMAIL = "data_email";
    public static final String KEY_PREFERENCES_DATA_USER_ID = "data_user_id";
    public static final String KEY_PREFERENCES_DATA_FIRST_NAME = "data_first_name";
    public static final String KEY_PREFERENCES_DATA_LAST_NAME = "data_last_name";
    public static final String KEY_PREFERENCES_DATA_NAME = "data_name";
    public static final String KEY_PREFERENCES_DATA_IMAGE_PROFILE = "data_image";

    /**
     * DATABASE      *
     */
    public static final String DB_NAME = "placego.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_USER = "user_position";

    public static final String USER_ID = "user_id";
    public static final String USER_NAME = "name";
    public static final String USER_TELEPHONE = "phone";
    public static final String USER_LAT = "lat";
    public static final String USER_LNG = "lng";
    public static final String USER_IMAGE = "lng";

    public final static String ACCOUNT_EXISTS = "account_exist";


}
