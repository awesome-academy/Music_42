package com.framgia.quangtran.music_42.data.repository;

import android.content.ContentResolver;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

public class TrackRepository implements TrackDataSource.Local, TrackDataSource.Remote {
    private static TrackRepository sInstance;
    private TrackDataSource.Remote mRemoteDataSource;
    private TrackDataSource.Local mLocalDataSource;

    private TrackRepository(TrackDataSource.Remote remoteDataSource,
                            TrackDataSource.Local localDataSource) {
        mRemoteDataSource = remoteDataSource;
        mLocalDataSource = localDataSource;
    }

    public static synchronized TrackRepository
    getInstance(TrackDataSource.Remote remoteDataSource,
                TrackDataSource.Local localDataSource) {
        if (sInstance == null) {
            sInstance = new TrackRepository(remoteDataSource, localDataSource);
        }
        return sInstance;
    }

    @Override
    public void getOnlineTrack(String api, TrackDataSource.DataCallback<Track> callback) {
        mRemoteDataSource.getOnlineTrack(api, callback);
    }

    @Override
    public void getOfflineTracks(ContentResolver contentResolver,
                                 TrackDataSource.DataCallback<Track> callback) {
        mLocalDataSource.getOfflineTracks(contentResolver, callback);
    }
}