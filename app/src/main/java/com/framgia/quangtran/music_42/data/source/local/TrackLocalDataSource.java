package com.framgia.quangtran.music_42.data.source.local;

import android.content.ContentResolver;
import android.util.Log;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

public class TrackLocalDataSource implements TrackDataSource.Local {
    private static TrackLocalDataSource sInstance;

    private TrackLocalDataSource() {
    }

    public static TrackLocalDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new TrackLocalDataSource();
        }
        return sInstance;
    }

    @Override
    public void getOfflineTracks(ContentResolver contentResolver,
                                 TrackDataSource.DataCallback<Track> callback) {
        new TrackStorageAsyncTask(contentResolver, callback).execute();
    }
}
