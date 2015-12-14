package com.placego.util;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by juanes on 23/11/15.
 */
public class ItemUser implements BaseColumns {

    public static final String CONTENT_AUTHORITY = "com.placego";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String CONTENT_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.placego.plans";

    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.placego.plan";

    public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(StringUtil.TABLE_USER).build();
}

