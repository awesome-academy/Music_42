package com.framgia.quangtran.music_42.data.source.remote;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

public class TrackRemoteDataSource implements TrackDataSource.Remote {
    private static TrackRemoteDataSource sInstance;

    private TrackRemoteDataSource() {
    }

    public static TrackRemoteDataSource getInstance() {
        if (sInstance == null) {
            sInstance = new TrackRemoteDataSource();
        }
        return sInstance;
    }

    @Override
    public void getOnlineTrack(String api, TrackDataSource.DataCallback<Track> callback) {
        new LoadTrackAsyncTask(callback).execute(api);
    }

    @Override
    public void searchTracks(String api, TrackDataSource.DataCallback<Track> callback) {
        new SearchAsyncTask(callback).execute(api);
    }

    public void getDetailTrack(String api, TrackDataSource.DataCallback<Track> callback) {
        new LoadTrackDetailTask(callback).execute(api);
    }
}
