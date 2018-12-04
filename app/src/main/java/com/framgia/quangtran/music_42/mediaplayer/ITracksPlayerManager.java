package com.framgia.quangtran.music_42.mediaplayer;

import com.framgia.quangtran.music_42.data.model.Track;

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

    long getDuration();

    long getCurrentPosition();

    void setTrackCurrentPosition(int position);

    int getTrackCurrentPosition();

    void setTracks(List<Track> tracks);
}
