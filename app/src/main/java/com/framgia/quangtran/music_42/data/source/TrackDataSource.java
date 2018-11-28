package com.framgia.quangtran.music_42.data.source;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface TrackDataSource {
    interface DataCallback<T> {
        void onSuccess(List<T> data);

        void onFailed(String message);
    }

    interface Local {
        void getOfflineTracks(DataCallback<Track> callback);
    }

    interface Remote {
        void getOnlineTrack(String api, DataCallback<Track> callback);
    }
}
