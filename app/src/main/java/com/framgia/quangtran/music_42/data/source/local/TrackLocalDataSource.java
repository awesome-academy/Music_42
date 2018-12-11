package com.framgia.quangtran.music_42.data.source.local;

import android.content.ContentResolver;
import android.content.Context;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

public class TrackLocalDataSource implements TrackDataSource.Local {
    private static final String EXIST_TRACK = "Exist track in favorite";
    private static TrackLocalDataSource sInstance;
    private static FavoriteTrackDbHelper sDbHelper;
    private ContentResolver mContentResolver;

    private TrackLocalDataSource(ContentResolver contentResolver) {
        mContentResolver = contentResolver;
    }

    public static TrackLocalDataSource getInstance(Context context, ContentResolver contentResolver) {
        sDbHelper = new FavoriteTrackDbHelper(context);
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

    @Override
    public void getFavoriteTracks(TrackDataSource.DataCallback<Track> callback) {
        sDbHelper.getTracks(callback);
    }

    @Override
    public void addFavoriteTrack(Track track, TrackDataSource.DataCallback<Boolean> callback) {
        if (sDbHelper.canAddTrack(track)) {
            sDbHelper.putTrack(track, callback);
            return;
        }
        callback.onFailed(EXIST_TRACK);
    }

    @Override
    public void deleteFavoriteTrack(Track track, TrackDataSource.DataCallback<Boolean> callback) {
        sDbHelper.deleteTrack(track, callback);
    }
}
