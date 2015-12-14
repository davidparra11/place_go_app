package com.placego.util;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created by juanes on 23/11/15.
 */
public class PlaceGoProvider extends ContentProvider {

    DataBase mDatabaseHelper;

    private static final int ROUTE_USER = 10;
    private static final int ROUTE_USER_ID = 11;


    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String AUTHORITY = ItemUser.CONTENT_AUTHORITY;
        matcher.addURI(AUTHORITY, "user_position", ROUTE_USER);
        matcher.addURI(AUTHORITY, "user_positions/*", ROUTE_USER_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = DataBase.getInstance(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ROUTE_USER:
                return ItemUser.CONTENT_TYPE;
            case ROUTE_USER_ID:
                return ItemUser.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        final SelectionBuilder builder = new SelectionBuilder();
        int uriMatch = sUriMatcher.match(uri);
        switch (uriMatch) {
            case ROUTE_USER_ID:
                final String id = uri.getLastPathSegment();
                builder.where(ItemUser._ID + "=?", id);
            case ROUTE_USER:
                builder.table(StringUtil.TABLE_USER)
                        .where(selection, selectionArgs);
                final Cursor c = builder.query(db, projection, sortOrder);
                final Context ctx = getContext();
                assert ctx != null;
                c.setNotificationUri(ctx.getContentResolver(), uri);
                return c;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        assert db != null;
        final int match = sUriMatcher.match(uri);
        Uri result;
        switch (match) {
            case ROUTE_USER:
                long id = db.insertOrThrow(StringUtil.TABLE_USER, null, values);
                result = Uri.parse(ItemUser.CONTENT_URI + "/" + id);
                break;
            case ROUTE_USER_ID:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_USER:
                count = builder.table(StringUtil.TABLE_USER)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            case ROUTE_USER_ID:
                final String id = uri.getLastPathSegment();
                count = builder.table(StringUtil.TABLE_USER)
                        .where(ItemUser._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .delete(db);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        final Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int count;
        switch (match) {
            case ROUTE_USER:
                count = builder.table(StringUtil.TABLE_USER)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case ROUTE_USER_ID:
                final String id = uri.getLastPathSegment();
                count = builder.table(StringUtil.TABLE_USER)
                        .where(ItemUser._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        final Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }

    static class DataBase extends SQLiteOpenHelper {

        private final static String INTEGER = " INTEGER, ";
        private final static String TEXT = " TEXT, ";
        private final static String DOUBLE = " DOUBLE, ";

        private static volatile DataBase instance;

        public static DataBase getInstance(Context context) {
            DataBase localInstance = instance;
            if (localInstance == null) {
                synchronized (DataBase.class) {
                    localInstance = instance;
                    if (localInstance == null) {
                        instance = localInstance = new DataBase(context);
                    }
                }
            }
            return instance;
        }

        final static String sqlUser = "CREATE TABLE " + StringUtil.TABLE_USER + " (" +
                ItemUser._ID + " INTEGER PRIMARY KEY," +
                StringUtil.USER_ID + INTEGER +
                StringUtil.USER_NAME + TEXT +
                StringUtil.USER_TELEPHONE + TEXT +
                StringUtil.USER_LAT + DOUBLE +
                StringUtil.USER_LNG + DOUBLE +
                StringUtil.USER_IMAGE + " TEXT);";

        DataBase(Context context) {
            super(context, StringUtil.DB_NAME, null, StringUtil.DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(sqlUser);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        }
    }
}
