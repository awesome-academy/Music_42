package com.framgia.quangtran.music_42.ui.search;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.framgia.quangtran.music_42.ui.LoadMoreAbstract;
import com.framgia.quangtran.music_42.ui.UIPlayerListener;
import com.framgia.quangtran.music_42.ui.home.adapters.TodayAdapter;
import com.framgia.quangtran.music_42.ui.play.PlayActivity;
import com.framgia.quangtran.music_42.util.StringUtil;

import java.util.List;

public class SearchActivity extends LoadMoreAbstract
        implements SearchContract.View, View.OnClickListener,
        TodayAdapter.ClickTrackElement, ServiceConnection, UIPlayerListener.ControlListener {
    private static final String CLEAN = "";
    private EditText mEditInput;
    private ImageView mImageBack;
    private ImageView mImageClear;
    private Button mButtonNextSong;
    private Button mButtonPreviousSong;
    private Button mButtonPlaySong;
    private ImageView mImageTrack;
    private ConstraintLayout mMiniPlayer;
    private TextView mTextSongName;
    private TextView mTextSingerName;
    private ProgressBar mProgressBar;
    private TodayAdapter mTodayAdapter;
    private SearchPresenter mPresenter;
    private List<Track> mTracks;
    private TracksService mService;
    private TracksServiceManager mTracksServiceManager;
    private int mOffset;
    private String mKeyword;

    public static Intent getSearchIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
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
        mTracksServiceManager.unbindService();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViewLoadMore();
        connectService();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_clear:
                mEditInput.setText(CLEAN);
                break;
            case R.id.image_back:
                super.onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void loadMoreData() {
        mIsScrolling = false;
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.searchTracks(StringUtil.initSearchApi(mKeyword, ++mOffset));
    }

    @Override
    public void initViewLoadMore() {
        mEditInput = findViewById(R.id.edit_search);
        mImageBack = findViewById(R.id.image_back);
        mImageClear = findViewById(R.id.image_clear);
        mProgressBar = findViewById(R.id.progress_search_more);
        mTextSongName = findViewById(R.id.text_song_name);
        mTextSingerName = findViewById(R.id.text_singer_name);
        mButtonNextSong = findViewById(R.id.button_next);
        mButtonPlaySong = findViewById(R.id.button_play);
        mButtonPreviousSong = findViewById(R.id.button_back);
        mImageTrack = findViewById(R.id.image_avatar);
        mMiniPlayer = findViewById(R.id.mini_player);
        initPresenter();
        mRecyclerView = findViewById(R.id.recycler_search);
        setListener();
        setLoadMore();
        initRecyclerView();
    }

    private void initRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(this);
        mTodayAdapter = new TodayAdapter(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mTodayAdapter);
        mProgressBar.setVisibility(View.GONE);
    }

    private void initPresenter() {
        TrackRepository repository = TrackRepository.getInstance(
                TrackRemoteDataSource.getInstance(),
                TrackLocalDataSource.getInstance(getApplicationContext(), getContentResolver()));
        mPresenter = new SearchPresenter(repository, this);
    }

    @Override
    public void showResult(List<Track> tracks) {
        Intent serviceIntent = TracksService.getMyServiceIntent(SearchActivity.this);
        if (mService == null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else startService(serviceIntent);
        }
        if (mOffset == 0) {
            mTodayAdapter.updateTracks(tracks);
            mTracks = tracks;
        } else {
            mTodayAdapter.addTracks(tracks);
            mTracks.addAll(tracks);
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showNoResult(String message) {
    }

    private void setListener() {
        mImageBack.setOnClickListener(this);
        mImageClear.setOnClickListener(this);
        mImageTrack.setOnClickListener(this);
        mButtonNextSong.setOnClickListener(this);
        mButtonPreviousSong.setOnClickListener(this);
        mButtonPlaySong.setOnClickListener(this);
        mEditInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence keyword, int i, int i1, int i2) {
                mOffset = 0;
                mKeyword = keyword.toString();
                if (mKeyword.isEmpty())
                    mImageClear.setImageResource(R.drawable.ic_skip_next_black_24dp);
                else mImageClear.setImageResource(R.drawable.ic_remove_black_24dp);
                mPresenter.searchTracks(StringUtil.initSearchApi(mKeyword, 0));
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
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

    @Override
    public void onClickTrack(List<Track> tracks, int i) {
        mService.setTracks(mTracks);
        mService.setPositionTrack(i);
        mService.play(i);
        if (mMiniPlayer.getVisibility() == View.GONE) {
            mMiniPlayer.setVisibility(View.VISIBLE);
            startActivity(PlayActivity.getPlayIntent(this));
        }
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
}
