package com.framgia.quangtran.music_42.data.source.remote;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;
import com.framgia.quangtran.music_42.util.StringUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchAsyncTask extends BaseLoadTracksAsyncTask<Track> {
    private static final String ARTWORK_URL = "artwork_url";
    public static final String ID = "id";
    private static final String KEY_USER = "user";
    private static final String KEY_USER_NAME = "username";
    private static final String TITLE = "title";
    private static final String DOWNLOADABLE = "downloadable";
    private static final String DOWNLOAD_URL = "download_url";

    public SearchAsyncTask(TrackDataSource.DataCallback<Track> callback) {
        super(callback);
    }

    @Override
    public List<Track> convertJson(String response) {
        List<Track> tracks = new ArrayList<>();
        try {
            JSONArray result = new JSONArray(response);
            for (int i = 0; i < result.length(); i++) {
                JSONObject trackInfo = result.getJSONObject(i);
                int id = trackInfo.getInt(ID);
                String title = trackInfo.getString(TITLE);
                String artworkUrl = trackInfo.getString(ARTWORK_URL);
                String artist = trackInfo.getJSONObject(KEY_USER)
                        .getString(KEY_USER_NAME);
                boolean isDownloadable = trackInfo.getBoolean(DOWNLOADABLE);
                String downloadUrl = null;
                if (isDownloadable) {
                    downloadUrl = StringUtil.initDownloadApi(trackInfo.getString(DOWNLOAD_URL));
                }
                Track track = new Track();
                track.setId(id);
                track.setTitle(title);
                track.setUserName(artist);
                track.setArtWorkUrl(artworkUrl);
                track.setStreamUrl(StringUtil.initStreamApi(id));
                track.setDownLoadUrl(downloadUrl);
                track.setDownload(isDownloadable);
                tracks.add(track);
            }
        } catch (JSONException e) {
            mException = e;
        }
        return tracks;
    }
}
