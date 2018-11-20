package com.framgia.quangtran.music_42.data.source;

import com.framgia.quangtran.music_42.data.model.Track;

public interface TrackDataSource {
    interface DataCallback<T> {
        void onSuccess(T data);

        void onFailed(String message);
    }

    interface Local {
        void getOfflineTracks(DataCallback<Track> callback);
    }

    interface Remote {
    }
}
