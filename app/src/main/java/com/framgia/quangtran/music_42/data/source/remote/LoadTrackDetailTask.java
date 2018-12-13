package com.framgia.quangtran.music_42.data.source.remote;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;
import com.framgia.quangtran.music_42.util.StringUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoadTrackDetailTask extends BaseLoadTracksAsyncTask<Track> {
    private static final String ARTWORK_URL = "artwork_url";
    public static final String ID = "id";
    private static final String KEY_USER = "user";
    private static final String KEY_USER_NAME = "username";
    private static final String TITLE = "title";
    private static final String DOWNLOADABLE = "downloadable";
    private static final String DOWNLOAD_URL = "download_url";

    public LoadTrackDetailTask(TrackDataSource.DataCallback<Track> callback) {
        super(callback);
    }

    @Override
    public List<Track> convertJson(String jsonString) {
        List<Track> tracks = new ArrayList<>();
        try {
            JSONObject track = new JSONObject(jsonString);
            int id = track.getInt(ID);
            String title = track.getString(TITLE);
            String artworkUrl = track.getString(ARTWORK_URL);
            String artist = track.getJSONObject(KEY_USER)
                    .getString(KEY_USER_NAME);
            boolean isDownloadable = track.getBoolean(DOWNLOADABLE);
            String downloadUrl = null;
            if (isDownloadable) {
                downloadUrl = StringUtil.initDownloadApi(track.getString(DOWNLOAD_URL));
            }
            Track recent_track = new Track();
            recent_track.setId(id);
            recent_track.setTitle(title);
            recent_track.setUserName(artist);
            recent_track.setArtWorkUrl(artworkUrl);
            recent_track.setDownload(isDownloadable);
            recent_track.setDownLoadUrl(downloadUrl);
            recent_track.setStreamUrl(StringUtil.initStreamApi(id));
            tracks.add(recent_track);
        } catch (JSONException e) {
            mException = e;
        }
        return tracks;
    }
}
