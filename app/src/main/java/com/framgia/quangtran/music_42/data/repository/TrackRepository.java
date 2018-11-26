package com.framgia.quangtran.music_42.data.repository;

import android.content.ContentResolver;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Genre;
import com.framgia.quangtran.music_42.data.model.GenreKey;
import com.framgia.quangtran.music_42.data.model.GenreName;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.util.ArrayList;
import java.util.List;

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

    public List<Genre> genreRepository() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(GenreName.ALL_MUSIC,
                GenreKey.ALL_MUSIC, R.drawable.allmusic));
        genres.add(new Genre(GenreName.ALL_AUDIO,
                GenreKey.ALL_AUDIO, R.drawable.alldaudio));
        genres.add(new Genre(GenreName.ALTERNATIVE,
                GenreKey.ALTERNATIVE, R.drawable.alternative));
        genres.add(new Genre(GenreName.AMBIENT,
                GenreKey.AMBIENT, R.drawable.ambient));
        genres.add(new Genre(GenreName.CLASSICAL,
                GenreKey.CLASSICAL, R.drawable.classical));
        genres.add(new Genre(GenreName.COUNTRY,
                GenreKey.COUNTRY, R.drawable.country));
        return genres;
    }

    @Override
    public void getOfflineTracks(ContentResolver contentResolver,
                                 TrackDataSource.DataCallback<Track> callback) {
        mLocalDataSource.getOfflineTracks(contentResolver, callback);
    }
}
