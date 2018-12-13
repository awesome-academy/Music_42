package com.framgia.quangtran.music_42.data.source;

import android.content.Context;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface TrackDataSource {
    interface DataCallback<T> {
        void onSuccess(List<T> data);

        void onFailed(String message);
    }

    interface Local {
        void getOfflineTracks(DataCallback<Track> callback);

        void getFavoriteTracks(DataCallback<Track> callback);

        void addFavoriteTrack(Track track, DataCallback<Boolean> callback);

        void deleteFavoriteTrack(Track track, DataCallback<Boolean> callback);

        void getRecentTrack(Context context, TrackDataSource.DataCallback<Long> callback);
    }

    interface Remote {
        void getOnlineTrack(String api, DataCallback<Track> callback);

        void searchTracks(String api, DataCallback<Track> callback);

        void getDetailTrack(String api, TrackDataSource.DataCallback<Track> callback);
    }
}
