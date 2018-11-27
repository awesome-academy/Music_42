package com.framgia.quangtran.music_42.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.mediaplayer.TracksPlayerManager;

import java.util.ArrayList;
import java.util.List;

public class MyService extends Service implements ServiceInterface {
    private final IBinder mIBinder = new LocalBinder();
    private TracksPlayerManager mTracksPlayerManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTracksPlayerManager = TracksPlayerManager.getInstance(this);
        mTracksPlayerManager.initMediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void start() {
        play(mTracksPlayerManager.getTrackCurrentPosition());
        mTracksPlayerManager.start();
    }

    @Override
    public void pause() {
        mTracksPlayerManager.pause();
    }

    @Override
    public void next() {
        mTracksPlayerManager.next();
    }

    @Override
    public void previous() {
        mTracksPlayerManager.previous();
    }

    @Override
    public void seekTo(int msec) {
        mTracksPlayerManager.seekTo(msec);
    }

    @Override
    public long getCurrentPosition() {
        return mTracksPlayerManager.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mTracksPlayerManager.getDuration();
    }

    @Override
    public void setTracks(List<Track> tracks) {
        mTracksPlayerManager.setTracks((ArrayList<Track>) tracks);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    public static Intent getMyServiceIntent(Context context) {
        Intent intent = new Intent(context, MyService.class);
        return intent;
    }

    public void play(int position) {
        mTracksPlayerManager.initMediaPlayer();
        mTracksPlayerManager.setTrackCurrentPosition(position);
        mTracksPlayerManager.initPlay(position);
    }
}
