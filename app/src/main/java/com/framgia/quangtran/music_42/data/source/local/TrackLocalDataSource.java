package com.framgia.quangtran.music_42.data.source.local;

import android.content.ContentResolver;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

public class TrackLocalDataSource implements TrackDataSource.Local {
    private static TrackLocalDataSource sInstance;
    private ContentResolver mContentResolver;

    private TrackLocalDataSource(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public static TrackLocalDataSource getInstance(ContentResolver contentResolver) {
        if (sInstance == null) {
            sInstance = new TrackLocalDataSource(contentResolver);
        }
        return sInstance;
    }

    @Override
    public void getOfflineTracks(
            TrackDataSource.DataCallback<Track> callback) {
        new TrackStorageAsyncTask(mContentResolver, callback).execute();
    }
}
