package com.framgia.quangtran.music_42.mediaplayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.ui.UIPlayerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TracksPlayerManager extends TracksPlayerSetting implements ITracksPlayerManager
        , MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener {
    private static final String ARTWORK_DEFAULT_SIZE = "large";
    private static final String ARTWORK_MAX_SIZE = "t500x500";
    private static final int POSITION_DEFAULT = 0;
    private static TracksPlayerManager sInstance;
    private List<Track> mTracks;
    private List<UIPlayerListener.ControlListener> mControlListeners;
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private int mTrackCurrentPosition;
    private int mState;
    private Exception mException;

    public static TracksPlayerManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new TracksPlayerManager(context);
        }
        return sInstance;
    }

    private TracksPlayerManager(Context context) {
        super();
        mContext = context;
        mControlListeners = new ArrayList<>();
    }

    @Override
    public void initMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        } else {
            mMediaPlayer = new MediaPlayer();
        }
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        mMediaPlayer.setOnErrorListener(this);
    }

    @Override
    public void initPlay(int position) {
        if (!mTracks.isEmpty() && position >= 0) {
            Uri uri = Uri.parse(mTracks.get(position).getStreamUrl());
            try {
                mMediaPlayer.setDataSource(mContext, uri);
                mMediaPlayer.prepareAsync();
                setState(StatePlayerType.PREPARING);
            } catch (IOException e) {
                mException = e;
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        start();
    }

    @Override
    public void start() {
        setState(StatePlayerType.PLAYING);
        mMediaPlayer.start();
    }

    @Override
    public void pause() {
        setState(StatePlayerType.PAUSE);
        mMediaPlayer.pause();
    }

    @Override
    public void reset() {
        setState(StatePlayerType.IDLE);
        mMediaPlayer.reset();
    }

    @Override
    public void release() {
        setState(StatePlayerType.RELEASE);
        mMediaPlayer.release();
    }

    @Override
    public void stop() {
        setState(StatePlayerType.STOP);
        mMediaPlayer.stop();
    }

    @Override
    public void next() {
        if (mTrackCurrentPosition < mTracks.size() - 1) {
            mTrackCurrentPosition++;
            initMediaPlayer();
            initPlay(mTrackCurrentPosition);
        }
    }

    @Override
    public void previous() {
        if (mTrackCurrentPosition > 0) {
            mTrackCurrentPosition--;
            initMediaPlayer();
            initPlay(mTrackCurrentPosition);
        }
    }

    @Override
    public void setLoopType(int loopType) {
        super.setLoopType(loopType);
        notifySettingChanged();
    }

    @Override
    public void setShuffleType(int shuffleType) {
        super.setShuffleType(shuffleType);
        notifySettingChanged();
    }

    @Override
    public void seekTo(int msec) {
        mMediaPlayer.seekTo(msec);
    }

    @Override
    public boolean getStatus() {
        return mMediaPlayer.isPlaying();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getState() {
        return mState;
    }

    private void setState(int state) {
        mState = state;
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public Track getTrack() {
        return mTracks.get(getPositionTrack());
    }

    @Override
    public void setPositionTrack(int position) {
        mTrackCurrentPosition = position;
    }

    @Override
    public int getPositionTrack() {
        return mTrackCurrentPosition;
    }

    @Override
    public void setTracks(List<Track> tracks) {
        if (mTracks != null) {
            mTracks.clear();
        }
        mTracks = tracks;
    }

    @Override
    public List<Track> getTracks() {
        return mTracks;
    }

    @Override
    public void setTrackInfo(TextView title, TextView artist) {
        Track track = mTracks.get(getPositionTrack());
        title.setText(track.getTitle());
        artist.setText(track.getUserName());
    }

    @Override
    public void setPlayTrackInfo(TextView title, TextView artist,
                                 ImageView imageBackGround, ImageView imageArtwork) {
        Track track = mTracks.get(getPositionTrack());
        title.setText(track.getTitle());
        artist.setText(track.getUserName());
        setImageBackground(track, imageBackGround);
        setImageBackground(track, imageArtwork);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        switch (getLoopType()) {
            case TracksPlayerSetting.LoopType.NONE:
                next();
                break;
            case TracksPlayerSetting.LoopType.ONE:
                initMediaPlayer();
                initPlay(getPositionTrack());
                break;
            case TracksPlayerSetting.LoopType.ALL:
                if (getTracksSize() != 0 && getTracksSize() - 1
                        == getPositionTrack()) {
                    initMediaPlayer();
                    setPositionTrack(POSITION_DEFAULT);
                    initPlay(POSITION_DEFAULT);
                } else {
                    next();
                }
                break;
        }
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    private void setImageBackground(Track track, ImageView imageBackground) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.soundcloud);
        if (track.getArtWorkUrl() != null) {
            track.setArtWorkUrl(track.getArtWorkUrl()
                    .replace(ARTWORK_DEFAULT_SIZE, ARTWORK_MAX_SIZE));
            Glide.with(mContext)
                    .load(track.getArtWorkUrl())
                    .apply(requestOptions)
                    .into(imageBackground);
        } else {
            Glide.with(mContext)
                    .load(R.drawable.classical)
                    .apply(requestOptions)
                    .into(imageBackground);
        }
    }

    private void notifySettingChanged() {
        for (UIPlayerListener.ControlListener controlListener : mControlListeners) {
            controlListener.notifyLoopChanged(getLoopType());
            controlListener.notifyShuffleChanged(getShuffleType());
        }
    }

    public void addControlListener(UIPlayerListener.ControlListener controlListener) {
        mControlListeners.add(controlListener);
    }

    public void removeControlListener(UIPlayerListener.ControlListener controlListener) {
        mControlListeners.remove(controlListener);
    }

    public int getTracksSize() {
        return mTracks.size();
    }
}
