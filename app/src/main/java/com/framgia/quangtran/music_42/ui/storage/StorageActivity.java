package com.framgia.quangtran.music_42.ui.storage;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.local.TrackLocalDataSource;
import com.framgia.quangtran.music_42.data.source.remote.TrackRemoteDataSource;
import com.framgia.quangtran.music_42.mediaplayer.ITracksPlayerManager;
import com.framgia.quangtran.music_42.service.TracksService;
import com.framgia.quangtran.music_42.service.TracksServiceManager;
import com.framgia.quangtran.music_42.ui.UIPlayerListener;
import com.framgia.quangtran.music_42.ui.play.PlayActivity;
import com.framgia.quangtran.music_42.ui.storage.contract.StorageStyle;

import java.util.List;

public class StorageActivity extends AppCompatActivity implements StorageContract.View,
        StorageAdapter.StorageClickListener, UIPlayerListener.ControlListener,
        View.OnClickListener, ServiceConnection {
    private static final String EXIST_TRACK = "Exist track in favorite";
    private TracksServiceManager mTracksServiceManager;
    private TracksService mService;
    private int mPosition;
    private String mKey;
    private List<Track> mTracks;
    private Track mTrack;
    private StorageContract.Presenter mPresenter;
    private ContentResolver mResolver;
    private ConstraintLayout mMiniPlayer;
    private TextView mTextTitle;
    private TextView mTextSongName;
    private TextView mTextSingerName;
    private ImageView mImageTrack;
    private Button mButtonNextSong;
    private Button mButtonPreviousSong;
    private Button mButtonPlaySong;
    private android.support.v7.widget.Toolbar mToolbar;

    public static Intent getStorageIntent(Context context, String key) {
        Intent intent = new Intent(context, StorageActivity.class);
        intent.putExtra(StorageStyle.StorageKey.KEY, key);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_personal);
        initUI();
        connectService();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mService.getStateMedia() != ITracksPlayerManager.StatePlayerType.PLAYING) {
            mButtonPlaySong.setBackgroundResource(R.drawable.ic_play_button);
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = ((TracksService.LocalBinder) iBinder).getService();
        mService.addControlListener(this);
        if (mService.getStateMiniPlayer() == true) {
            mMiniPlayer.setVisibility(View.VISIBLE);
            setItemMiniPlayer(mService.getTrack());
        }
        if (mService.getStateMedia() != ITracksPlayerManager.StatePlayerType.PLAYING) {
            mButtonPlaySong.setBackgroundResource(R.drawable.ic_play_button);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }

    @Override
    public void onSuccess(List<Track> data) {
        mTracks = data;
        if (data != null) {
            initRecycler(data);
        }
    }

    @Override
    public void onFailure(String message) {
        if (message.equals(EXIST_TRACK)) {
            mPresenter.deleteFavoriteTrack(mTrack);
        }
    }

    @Override
    public void addFavoriteTrackSuccess() {
        if (!mKey.equals(StorageStyle.StorageKey.FAVORITE)) {
            Toast.makeText(this, R.string.text_add_success, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deleteFavoriteTrackSuccess() {
        if (mKey.equals(StorageStyle.StorageKey.FAVORITE)) {
            mPresenter.getFavoriteTracks();
        }
        Toast.makeText(this, R.string.text_delete_favorite, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickPlayMusic(int i) {
        mPosition = i;
        mService.setTracks(mTracks);
        mService.setPositionTrack(mPosition);
        mService.play(mPosition);
        if (mMiniPlayer.getVisibility() == View.GONE) {
            mMiniPlayer.setVisibility(View.VISIBLE);
            startActivity(PlayActivity.getPlayIntent(this));
        }
    }

    @Override
    public void onClickImageFavorite(Track track, int i) {
        mTrack = track;
        mPresenter.addFavoriteTrack(track);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_avatar:
                startActivity(PlayActivity.getPlayIntent(this));
                break;
            case R.id.button_back:
                mService.previous();
                mButtonPlaySong.setBackgroundResource(R.drawable.ic_pause);
                break;
            case R.id.button_play:
                setButtonPlay();
                break;
            case R.id.button_next:
                mService.next();
                mButtonPlaySong.setBackgroundResource(R.drawable.ic_pause);
                break;
            default:
                break;
        }
    }

    @Override
    public void notifyShuffleChanged(int shuffleType) {
    }

    @Override
    public void notifyLoopChanged(int loopType) {
    }

    @Override
    public void notifyStateChanged(Track track, int stateType) {
        switch (stateType) {
            case ITracksPlayerManager.StatePlayerType.IDLE:
                break;
            case ITracksPlayerManager.StatePlayerType.PLAYING:
                setItemMiniPlayer(track);
                mButtonPlaySong.setBackgroundResource(R.drawable.ic_pause);
                break;
            case ITracksPlayerManager.StatePlayerType.PAUSE:
                setItemMiniPlayer(track);
                mButtonPlaySong.setBackgroundResource(R.drawable.ic_play_button);
                break;
            default:
                setItemMiniPlayer(track);
                break;
        }
    }

    private void initUI() {
        mKey = getIntent().getStringExtra(StorageStyle.StorageKey.KEY);
        mTextTitle = findViewById(R.id.text_title);
        mTextTitle.setText(mKey);
        mTextSongName = findViewById(R.id.text_song_name);
        mTextSingerName = findViewById(R.id.text_singer_name);
        mImageTrack = findViewById(R.id.image_avatar);
        mButtonNextSong = findViewById(R.id.button_next);
        mButtonPlaySong = findViewById(R.id.button_play);
        mButtonPreviousSong = findViewById(R.id.button_back);
        mToolbar = findViewById(R.id.tool_bar);
        mMiniPlayer = findViewById(R.id.mini_player);
        setOnClickListener();
        initRepository();
        getTracks();
    }

    private void setOnClickListener() {
        mImageTrack.setOnClickListener(this);
        mButtonNextSong.setOnClickListener(this);
        mButtonPreviousSong.setOnClickListener(this);
        mButtonPlaySong.setOnClickListener(this);
        setOnClickToolBar();
    }

    private void initRepository() {
        mResolver = getApplication().getContentResolver();
        TrackRepository repository = TrackRepository.getInstance(TrackRemoteDataSource
                .getInstance(), TrackLocalDataSource.getInstance(getApplicationContext(), mResolver));
        mPresenter = new StoragePresenter(repository);
        mPresenter.setView(this);
    }

    private void setOnClickToolBar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getTracks() {
        if (mKey.equals(StorageStyle.StorageKey.LOCAL)) {
            mPresenter.getOfflineTracks();
        } else if (mKey.equals(StorageStyle.StorageKey.DOWNLOAD)) {
            mPresenter.getDownloadTracks();
        } else {
            mPresenter.getFavoriteTracks();
        }
    }

    private void initRecycler(List<Track> tracks) {
        RecyclerView mRecyclerLocal = findViewById(R.id.recycler_music);
        mRecyclerLocal.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        StorageAdapter storageAdapter = new StorageAdapter(mKey, tracks, this);
        mRecyclerLocal.setAdapter(storageAdapter);
    }

    private void connectService() {
        ServiceConnection connection = this;
        Intent intent = new Intent(this, TracksService.class);
        mTracksServiceManager = new TracksServiceManager(this, intent, connection,
                Context.BIND_AUTO_CREATE);
        mTracksServiceManager.bindService();
    }

    private void setItemMiniPlayer(Track track) {
        Glide.with(getApplicationContext())
                .load(track.getArtWorkUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.soundcloud)
                        .error(R.drawable.soundcloud))
                .into(mImageTrack);
        mTextSongName.setText(track.getTitle());
        mTextSingerName.setText(track.getUserName());
    }

    private void setButtonPlay() {
        if (mService.getStateMedia() == ITracksPlayerManager.StatePlayerType.PLAYING) {
            mService.pause();
            mButtonPlaySong.setBackgroundResource(R.drawable.ic_play_button);
        } else {
            mService.start();
            mButtonPlaySong.setBackgroundResource(R.drawable.ic_pause);
        }
    }
}
