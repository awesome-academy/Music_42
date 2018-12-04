package com.framgia.quangtran.music_42.ui.play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.mediaplayer.ITracksPlayerManager;
import com.framgia.quangtran.music_42.service.TracksITracksService;
import com.framgia.quangtran.music_42.service.TracksServiceManager;
import com.framgia.quangtran.music_42.util.TimeUtils;

import java.util.List;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, TrackAdapter.ClickTrackElement, TrackAdapter.OnDragDropListener {
    private static final int PROGRESS_START = 0;
    private static final int PROGRESS_MAX = 100;
    private static final int DELAY_TIME = 1000;
    private ImageView mImageShuffle;
    private ImageView mImagePrevious;
    private ImageView mImageNext;
    private ImageView mImagePlay;
    private ImageView mImageLoop;
    private ImageView mImageNowPlaying;
    private ImageView mImageClose;
    private ImageView mImageBackGround;
    private ImageView mImageArtwork;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private TextView mTextTitle;
    private TextView mTextArtist;
    private DrawerLayout mDrawerLayout;
    private TracksITracksService mService;
    private TimeUtils mTimeUtils;
    private TrackAdapter mTrackAdapter;
    private android.os.Handler mHandler = new android.os.Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        startService();
        initUI();
    }

    public static Intent getPlayIntent(Context context) {
        Intent intent = new Intent(context, PlayActivity.class);
        return intent;
    }

    private void initUI() {
        mDrawerLayout = findViewById(R.id.drawer_play);
        mImageShuffle = findViewById(R.id.image_shuffle);
        mImagePrevious = findViewById(R.id.image_previous);
        mImagePlay = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImageLoop = findViewById(R.id.image_loop);
        mImageNowPlaying = findViewById(R.id.image_now_playing);
        mImageClose = findViewById(R.id.image_close);
        mImageBackGround = findViewById(R.id.image_background);
        mImageArtwork = findViewById(R.id.image_artwork);
        mTextTitle = findViewById(R.id.text_title);
        mTextArtist = findViewById(R.id.text_artist);
        mSeekBar = findViewById(R.id.seek_bar_track);
        mSeekBar.setProgress(PROGRESS_START);
        mSeekBar.setMax(PROGRESS_MAX);
        mSeekBar.setOnSeekBarChangeListener(this);
        mCurrentTime = findViewById(R.id.text_current_time);
        mTotalTime = findViewById(R.id.text_total_time);
        setListener();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TracksITracksService.LocalBinder binder = (TracksITracksService.LocalBinder) iBinder;
            mService = binder.getService();
            startMusic();
            initNavigation();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            unbindService(mConnection);
        }
    };

    public void startService() {
        Intent startService = new Intent(this, TracksITracksService.class);
        TracksServiceManager tracksServiceManager = new TracksServiceManager(this, startService,
                mConnection, Context.BIND_AUTO_CREATE);
        tracksServiceManager.bindService();
        startService(startService);
        Intent serviceIntent = TracksITracksService.getMyServiceIntent(PlayActivity.this);
        if (mService == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }

    void startMusic() {
        mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
        mHandler.postDelayed(mUpdateTimes, DELAY_TIME);
    }

    public void setListener() {
        mImageShuffle.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mImageLoop.setOnClickListener(this);
        mImageClose.setOnClickListener(this);
        mImageNowPlaying.setOnClickListener(this);
        mCurrentTime.setOnClickListener(this);
        mTotalTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_shuffle:
                break;
            case R.id.image_previous:
                mService.previous();
                mImagePlay.setImageResource(R.drawable.ic_pause);
                mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
                break;
            case R.id.image_play:
                if (mService.getStateMedia() == ITracksPlayerManager.StatePlayerType.PLAYING) {
                    mService.pause();
                    mImagePlay.setImageResource(R.drawable.ic_play_button);
                } else {
                    mService.start();
                    mImagePlay.setImageResource(R.drawable.ic_pause);
                }
                break;
            case R.id.image_next:
                mService.next();
                mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
                mImagePlay.setImageResource(R.drawable.ic_pause);
                break;
            case R.id.image_loop:
                break;
            case R.id.image_now_playing:
                mDrawerLayout.openDrawer(Gravity.END);
                break;
            case R.id.image_close:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimes);
        if (mService != null) {
            int totalDuration = (int) mService.getDuration();
            int progress = mSeekBar.getProgress();
            int current = mTimeUtils.progressToTimer(progress, totalDuration);
            mService.seekTo(current);
            mHandler.postDelayed(mUpdateTimes, DELAY_TIME);
        }
    }

    private void initNavigation() {
        RecyclerView recyclerNow = findViewById(R.id.recycler_now_playing);
        mTrackAdapter = new TrackAdapter(mService.getTracks(), this, this);
        recyclerNow.setAdapter(mTrackAdapter);
    }


    private Runnable mUpdateTimes = (new Runnable() {
        @Override
        public void run() {
            mTimeUtils = new TimeUtils();
            if (mService.getStateMedia() == ITracksPlayerManager.StatePlayerType.PLAYING) {
                long currentTime = mService.getCurrentPosition();
                long totalTime = mService.getDuration();
                mCurrentTime.setText(mTimeUtils.TotalTime(currentTime));
                mTotalTime.setText(mTimeUtils.TotalTime(totalTime));
                int progress = (int) (mTimeUtils.getProgressPercentage(currentTime, totalTime));
                mSeekBar.setProgress(progress);
            }
            mHandler.postDelayed(this, PROGRESS_MAX);
        }
    });

    @Override
    public void onClickTrack(int i) {
        mService.play(i);
        mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
    }

    @Override
    public void onDropViewHolder(List<Track> tracks) {
        mService.setTracks(tracks);
    }

    @Override
    public void onSwipeViewHolder(List<Track> tracks) {
        mService.setTracks(tracks);
    }
}
