package com.framgia.quangtran.music_42.service;

import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.quangtran.music_42.data.model.Track;

import java.util.List;

public interface ITracksService {
    void start();

    void pause();

    void next();

    void previous();

    void seekTo(int msec);

    long getCurrentPosition();

    long getDuration();

    int getStateMedia();

    void setPositionTrack(int i);

    int getPositionTrack();

    Track getTrack();

    void setTracks(List<Track> tracks);

    List<Track> getTracks();

    void setTrackInfo(TextView title, TextView artist);

    void setPlayTrackInfo(TextView title, TextView artist, ImageView imageBackGround,
                          ImageView imageArtwork);
}
