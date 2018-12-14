package com.framgia.quangtran.music_42.ui.play;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.local.TrackLocalDataSource;
import com.framgia.quangtran.music_42.data.source.remote.TrackRemoteDataSource;
import com.framgia.quangtran.music_42.mediaplayer.ITracksPlayerManager;
import com.framgia.quangtran.music_42.mediaplayer.TracksPlayerSetting;
import com.framgia.quangtran.music_42.service.DownloadService;
import com.framgia.quangtran.music_42.service.TracksService;
import com.framgia.quangtran.music_42.service.TracksServiceManager;
import com.framgia.quangtran.music_42.ui.SimpleItemTouchHelperCallback;
import com.framgia.quangtran.music_42.ui.UIPlayerListener;
import com.framgia.quangtran.music_42.util.StringUtil;
import com.framgia.quangtran.music_42.util.TimeUtils;

import java.io.File;
import java.util.List;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, UIPlayerListener.ControlListener,
        SimpleItemTouchHelperCallback.ItemTouchListener, PlayContract.View,
        TrackAdapter.ClickTrackElement, TrackAdapter.OnDragDropListener {
    private static final String EXIST_TRACK = "Exist track in favorite";
    private static final String ROOT_FOLDER = "storage/emulated/0/download/";
    private static final String MP3_FORMAT = ".mp3";
    private static final int PROGRESS_START = 0;
    private static final int PROGRESS_MAX = 100;
    private static final int DELAY_TIME = 1000;
    private static final int REQUEST_PERMISSION = 10;
    private ImageView mImageShuffle;
    private ImageView mImagePrevious;
    private ImageView mImageNext;
    private ImageView mImagePlay;
    private ImageView mImageLoop;
    private ImageView mImageDownload;
    private ImageView mImageFavorite;
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
    private TracksService mService;
    private TimeUtils mTimeUtils;
    private TrackAdapter mTrackAdapter;
    private boolean mHasPermission;
    PlayContract.Presenter mPresenter;
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

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            TracksService.LocalBinder binder = (TracksService.LocalBinder) iBinder;
            mService = binder.getService();
            mService.addControlListener(PlayActivity.this);
            mService.setStateMiniPlayer(true);
            startMusic();
            initNavigation();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            unbindService(mConnection);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_shuffle:
                setShuffle();
                break;
            case R.id.image_previous:
                setButtonPrevious();
                break;
            case R.id.image_play:
                setButtonPlay();
                break;
            case R.id.image_next:
                setButtonNext();
                break;
            case R.id.image_loop:
                setLoop();
                break;
            case R.id.image_now_playing:
                mDrawerLayout.openDrawer(Gravity.END);
                break;
            case R.id.image_download:
                setButtonDownload();
                break;
            case R.id.image_favorite:
                addFavoriteTrack();
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
            int current = TimeUtils.progressToTimer(progress, totalDuration);
            mService.seekTo(current);
            mHandler.postDelayed(mUpdateTimes, DELAY_TIME);
        }
    }

    @Override
    public void notifyShuffleChanged(@TracksPlayerSetting.ShuffleType int shuffleType) {
        switch (shuffleType) {
            case TracksPlayerSetting.ShuffleType.ON:
                mImageShuffle.setImageResource(R.drawable.ic_shuffle_click);
                break;
            case TracksPlayerSetting.ShuffleType.OFF:
                mImageShuffle.setImageResource(R.drawable.ic_shuffle);
                break;
        }
    }

    @Override
    public void notifyLoopChanged(@TracksPlayerSetting.LoopType int loopType) {
        switch (loopType) {
            case TracksPlayerSetting.LoopType.NONE:
                mImageLoop.setImageResource(R.drawable.loop_none);
                break;
            case TracksPlayerSetting.LoopType.ONE:
                mImageLoop.setImageResource(R.drawable.loop_one);
                break;
            case TracksPlayerSetting.LoopType.ALL:
                mImageLoop.setImageResource(R.drawable.loop_all);
                break;
            default:
                break;
        }
    }

    @Override
    public void notifyStateChanged(Track track, int stateType) {
        switch (stateType) {
            case ITracksPlayerManager.StatePlayerType.IDLE:
                break;
            case ITracksPlayerManager.StatePlayerType.PLAYING:
                mImagePlay.setImageResource(R.drawable.ic_pause);
                mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
                break;
            case ITracksPlayerManager.StatePlayerType.PAUSE:
                mImagePlay.setImageResource(R.drawable.ic_play_button);
                break;
            case ITracksPlayerManager.StatePlayerType.STOP:
                mImagePlay.setImageResource(R.drawable.ic_play_button);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClickTrack(int i) {
        mService.play(i);
        mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
    }

    @Override
    public void onMove(int oldPosition, int newPosition) {
        mTrackAdapter.onMove(oldPosition, newPosition);
    }

    @Override
    public void swipe(int position, int direction) {
        mTrackAdapter.swipe(position);
    }

    @Override
    public void onDropViewHolder(List<Track> tracks) {
        mService.setTracks(tracks);
    }

    @Override
    public void onSwipeViewHolder(List<Track> tracks) {
        mService.setTracks(tracks);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] != PackageManager.PERMISSION_DENIED) {
                    mHasPermission = true;
                } else checkPermission();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailure(String message) {
        if (message.equals(EXIST_TRACK)) {
            mPresenter.deleteFavoriteTrack(mService.getTrack());
        }
    }

    @Override
    public void onDeleteTrackSuccess() {
        Toast.makeText(this, R.string.text_delete_favorite, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavoriteTrackSuccess() {
        Toast.makeText(this, R.string.text_add_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimes);
    }

    private void initUI() {
        mDrawerLayout = findViewById(R.id.drawer_play);
        mImageShuffle = findViewById(R.id.image_shuffle);
        mImagePrevious = findViewById(R.id.image_previous);
        mImagePlay = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImageLoop = findViewById(R.id.image_loop);
        mImageDownload = findViewById(R.id.image_download);
        mImageFavorite = findViewById(R.id.image_favorite);
        mImageNowPlaying = findViewById(R.id.image_now_playing);
        mImageClose = findViewById(R.id.image_close);
        mImageBackGround = findViewById(R.id.image_background);
        mImageArtwork = findViewById(R.id.image_artwork);
        mTextTitle = findViewById(R.id.text_title);
        mTextTitle.setSelected(true);
        mTextArtist = findViewById(R.id.text_artist);
        mSeekBar = findViewById(R.id.seek_bar_track);
        mSeekBar.setProgress(PROGRESS_START);
        mSeekBar.setMax(PROGRESS_MAX);
        mSeekBar.setOnSeekBarChangeListener(this);
        mCurrentTime = findViewById(R.id.text_current_time);
        mTotalTime = findViewById(R.id.text_total_time);
        setListener();
        setStatusBar();
    }

    public void startService() {
        Intent startService = new Intent(this, TracksService.class);
        TracksServiceManager tracksServiceManager = new TracksServiceManager(this, startService,
                mConnection, Context.BIND_AUTO_CREATE);
        tracksServiceManager.bindService();
        startService(startService);
        Intent serviceIntent = TracksService.getMyServiceIntent(PlayActivity.this);
        if (mService == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
        }
        bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);
    }

    private void setListener() {
        mImageShuffle.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mImageLoop.setOnClickListener(this);
        mImageDownload.setOnClickListener(this);
        mImageFavorite.setOnClickListener(this);
        mImageClose.setOnClickListener(this);
        mImageNowPlaying.setOnClickListener(this);
        mCurrentTime.setOnClickListener(this);
        mTotalTime.setOnClickListener(this);
    }

    private void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.color_black));
        }
    }

    private void startMusic() {
        mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
        mHandler.postDelayed(mUpdateTimes, DELAY_TIME);
    }

    private void setShuffle() {
        switch (mService.getShuffle()) {
            case TracksPlayerSetting.ShuffleType.OFF:
                mService.setShuffle(TracksPlayerSetting.ShuffleType.ON);
                break;
            case TracksPlayerSetting.ShuffleType.ON:
                mService.setShuffle(TracksPlayerSetting.ShuffleType.OFF);
                break;
        }
    }

    private void setLoop() {
        switch (mService.getLoop()) {
            case TracksPlayerSetting.LoopType.NONE:
                mService.setLoop(TracksPlayerSetting.LoopType.ONE);
                break;
            case TracksPlayerSetting.LoopType.ONE:
                mService.setLoop(TracksPlayerSetting.LoopType.ALL);
                break;
            case TracksPlayerSetting.LoopType.ALL:
                mService.setLoop(TracksPlayerSetting.LoopType.NONE);
                break;
        }
    }

    private void addFavoriteTrack() {
        TrackRepository repository = TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(getApplicationContext(), getContentResolver())
        );
        mPresenter = new PlayPresenter(repository, this);
        mPresenter.addFavoriteTrack(mService.getTrack());
    }

    private void setButtonPlay() {
        if (mService.getStateMedia() == ITracksPlayerManager.StatePlayerType.PLAYING) {
            mService.pause();
            mImagePlay.setImageResource(R.drawable.ic_play_button);
        } else {
            mService.start();
            mImagePlay.setImageResource(R.drawable.ic_pause);
        }
    }

    private void setButtonNext() {
        mService.next();
        mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
        mImagePlay.setImageResource(R.drawable.ic_pause);
    }

    private void setButtonPrevious() {
        mService.previous();
        mImagePlay.setImageResource(R.drawable.ic_pause);
        mService.setPlayTrackInfo(mTextTitle, mTextArtist, mImageBackGround, mImageArtwork);
    }

    private void setButtonDownload() {
        checkPermission();
        if (mHasPermission) {
            beginDownload();
            Toast.makeText(this, R.string.notify_begin_download,
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            Toast.makeText(this, R.string.error_download,
                    Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void checkPermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, permissions[0])
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, REQUEST_PERMISSION);
        } else mHasPermission = true;
    }

    private boolean isAcceptDownload(String title) {
        String fileName = StringUtil.append(ROOT_FOLDER, title, MP3_FORMAT);
        File file = new File(fileName);
        if (!file.isDirectory() && file.exists()) return confirmDownload();
        return true;
    }

    private boolean confirmDownload() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.notify_file_exists)
                .setMessage(R.string.notify_confirm_download)
                .setIcon(R.drawable.ic_download_false)
                .setPositiveButton(R.string.confirm_yes, (DialogInterface.OnClickListener) this)
                .setNegativeButton(R.string.confirm_no, (DialogInterface.OnClickListener) this)
                .create();
        dialog.show();
        return false;
    }

    private void beginDownload() {
        Intent intent = DownloadService.getDownloadIntent(this, mService.getTrack());
        startService(intent);
    }

    private void initNavigation() {
        RecyclerView recyclerNow = findViewById(R.id.recycler_now_playing);
        mTrackAdapter = new TrackAdapter(mService.getTracks(), this, this);
        recyclerNow.setAdapter(mTrackAdapter);
        initItemTouchHelper(recyclerNow);
    }

    private void initItemTouchHelper(RecyclerView recyclerView) {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
                int progress = (TimeUtils.getProgressPercentage(currentTime, totalTime));
                mSeekBar.setProgress(progress);
            }
            mHandler.postDelayed(this, PROGRESS_MAX);
        }
    });
}
