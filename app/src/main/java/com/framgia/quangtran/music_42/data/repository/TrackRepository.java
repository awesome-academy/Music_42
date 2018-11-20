package com.framgia.quangtran.music_42.data.repository;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

public class TrackRepository implements TrackDataSource.Local, TrackDataSource.Remote {
    @Override
    public void getOfflineTracks(TrackDataSource.DataCallback<Track> callback) {
    }
}
