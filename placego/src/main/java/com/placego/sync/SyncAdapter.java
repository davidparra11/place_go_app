package com.placego.sync;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;

import com.placego.util.ItemUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;


class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final int NET_CONNECT_TIMEOUT_MILLIS = 15000;
    private static final int NET_READ_TIMEOUT_MILLIS = 10000;
    private final ContentResolver mContentResolver;


    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        try {
            updateLocalFeedData(syncResult);


        } catch (MalformedURLException e) {
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            syncResult.stats.numIoExceptions++;
            return;
        } catch (RemoteException e) {
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            syncResult.databaseError = true;
            return;
        }
    }

    public void updateLocalFeedData(final SyncResult syncResult)
            throws IOException, RemoteException,
            OperationApplicationException {
        final ContentResolver contentResolver = getContext().getContentResolver();

        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        mContentResolver.applyBatch(ItemUser.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(ItemUser.CONTENT_URI, null, false);
    }
}
