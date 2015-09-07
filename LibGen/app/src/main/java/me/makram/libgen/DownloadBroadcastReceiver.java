package me.makram.libgen;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

/**
 * Created by admin on 9/7/15.
 */
public class DownloadBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = "DBR";

    DownloadManager downloadManager;

    public DownloadBroadcastReceiver(Context context) {
        downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences preferences = context.getSharedPreferences(DetailsActivity.class.getName(),
                Context.MODE_PRIVATE);
        long downloadId = preferences.getLong(DetailsActivity.ENTRY_DOWNLOADID_KEY, -1);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {

                String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                Log.i(TAG, "downloaded file " + uriString);
            } else {
                Log.i(TAG, "download failed " + cursor.getInt(columnIndex));
            }
        }
    }
}
