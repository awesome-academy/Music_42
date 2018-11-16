package com.framgia.quangtran.music_42.data.source.remote;

import android.os.AsyncTask;

import com.framgia.quangtran.music_42.data.source.TrackDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public abstract class BaseLoadTracksAsyncTask<T> extends AsyncTask<String, String, List<T>> {
    private static final String REQUEST_METHOD = "GET";
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 15000;
    protected TrackDataSource.DataCallback<T> mCallback;
    protected Exception mException;
    private HttpURLConnection mUrlConnection;

    public BaseLoadTracksAsyncTask(TrackDataSource.DataCallback<T> callback) {
        mCallback = callback;
    }

    @Override
    protected List<T> doInBackground(String... strings) {
        String respond = "";
        try {
            URL url = new URL(strings[0]);
            mUrlConnection = (HttpURLConnection) url.openConnection();
            mUrlConnection.setRequestMethod(REQUEST_METHOD);
            mUrlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            mUrlConnection.setReadTimeout(READ_TIMEOUT);
            mUrlConnection.connect();
            InputStream inputStream = mUrlConnection.getInputStream();
            respond = readResponse(inputStream);
        } catch (Exception e) {
            mException = e;
        }
        mUrlConnection.disconnect();
        return convertJson(respond);
    }

    @Override
    protected void onPostExecute(List<T> list) {
        super.onPostExecute(list);
        if (mException == null) {
            mCallback.onSuccess(list);
        } else {
            mCallback.onFailed(mException.getMessage());
        }
    }

    private String readResponse(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        if (inputStream != null) {
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        }
        return sb.toString();
    }

    public abstract List<T> convertJson(String jsonString);
}
