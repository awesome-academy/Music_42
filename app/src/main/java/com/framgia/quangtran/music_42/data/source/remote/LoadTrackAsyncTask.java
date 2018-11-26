package com.framgia.quangtran.music_42.data.source.remote;

import android.util.Log;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadTrackAsyncTask extends BaseLoadTracksAsyncTask<Track> {
    public static final String ID = "id";
    private static final String TITLE = "title";
    private static final String TRACK = "track";
    private static final String ARTWORK_URL = "artwork_url";
    private static final String COLLECTION = "collection";
    private static final String USER = "user";
    private static final String USER_NAME = "username";

    public LoadTrackAsyncTask(TrackDataSource.DataCallback<Track> callback) {
        super(callback);
    }

    @Override
    public List<Track> convertJson(String jsonString) {
        List<Track> tracks = new ArrayList<>();
        try {
            JSONObject result = new JSONObject(jsonString);
            JSONArray trackArray = result.getJSONArray(COLLECTION);
            for (int i = 0; i < trackArray.length(); i++) {
                JSONObject trackDetail = trackArray.getJSONObject(i);
                JSONObject trackInfo = trackDetail.getJSONObject(TRACK);
                int id = trackInfo.getInt(ID);
                String tittle = trackInfo.getString(TITLE);
                String artwork = trackInfo.getString(ARTWORK_URL);
                JSONObject userDetail = trackInfo.getJSONObject(USER);
                String fullname = userDetail.getString(USER_NAME);
                Track track = new Track();
                track.setId(id);
                track.setTitle(tittle);
                track.setArtWorkUrl(artwork);
                track.setUserName(fullname);
                tracks.add(track);
            }
        } catch (Exception e) {
            mException = e;
        }
        return tracks;
    }
}
