package com.framgia.quangtran.music_42.mediaplayer;

import android.support.annotation.IntDef;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.quangtran.music_42.data.model.Track;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

public interface ITracksPlayerManager {

    void initMediaPlayer();

    void initPlay(int position);

    void start();

    void pause();

    void reset();

    void release();

    void stop();

    void next();

    void previous();

    void seekTo(int msec);

    int getDuration();

    int getState();

    int getCurrentPosition();

    Track getTrack();

    void setPositionTrack(int position);

    int getPositionTrack();

    void setTracks(List<Track> tracks);

    List<Track> getTracks();

    void setTrackInfo(TextView title, TextView artist);

    void setPlayTrackInfo(TextView title, TextView artist, ImageView imageBackGround,
                          ImageView imageArtwork);

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({StatePlayerType.IDLE, StatePlayerType.PREPARING, StatePlayerType.PLAYING,
            StatePlayerType.PAUSE, StatePlayerType.STOP, StatePlayerType.RELEASE})
    @interface StatePlayerType {
        int IDLE = 0;
        int PREPARING = 1;
        int PLAYING = 2;
        int PAUSE = 3;
        int STOP = 4;
        int RELEASE = 5;
    }
}
