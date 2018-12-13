package com.framgia.quangtran.music_42.data.repository;

import android.content.Context;

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
    public void searchTracks(String api, TrackDataSource.DataCallback<Track> callback) {
        mRemoteDataSource.searchTracks(api, callback);
    }

    @Override
    public void getOfflineTracks(TrackDataSource.DataCallback<Track> callback) {
        mLocalDataSource.getOfflineTracks(callback);
    }

    @Override
    public void getFavoriteTracks(TrackDataSource.DataCallback<Track> callback) {
        mLocalDataSource.getFavoriteTracks(callback);
    }

    @Override
    public void addFavoriteTrack(Track track, TrackDataSource.DataCallback<Boolean> callback) {
        mLocalDataSource.addFavoriteTrack(track, callback);
    }

    @Override
    public void deleteFavoriteTrack(Track track, TrackDataSource.DataCallback<Boolean> callback) {
        mLocalDataSource.deleteFavoriteTrack(track, callback);
    }

    @Override
    public void getRecentTrack(Context context, TrackDataSource.DataCallback<Long> callback) {
        mLocalDataSource.getRecentTrack(context,callback);
    }

    @Override
    public void getDetailTrack(String api, TrackDataSource.DataCallback<Track> callback) {
        mRemoteDataSource.getDetailTrack(api,callback);
    }
}
