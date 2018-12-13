package com.framgia.quangtran.music_42.ui.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.mediaplayer.ITracksPlayerManager;
import com.framgia.quangtran.music_42.service.TracksService;
import com.framgia.quangtran.music_42.service.TracksServiceManager;
import com.framgia.quangtran.music_42.ui.UIPlayerListener;
import com.framgia.quangtran.music_42.ui.home.adapters.TodayAdapter;
import com.framgia.quangtran.music_42.ui.home.adapters.ViewPagerAdapter;
import com.framgia.quangtran.music_42.ui.play.PlayActivity;
import com.framgia.quangtran.music_42.ui.search.SearchActivity;
import com.framgia.quangtran.music_42.util.MarginUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ServiceConnection,
        TodayAdapter.ClickTrackElement, UIPlayerListener.ControlListener, View.OnClickListener {
    private static final String BUNDLE_TRACKS = "com.framgia.quangtran.music_42.ui.genre.BUNDLE_TRACKS";
    private static final int MARGIN_DEFAULT = 0;
    private static final int MARGIN_BOTTOM = 90;
    private int mTrackPosition;
    private ViewPager mViewPagerMusic;
    private TabLayout mTabLayout;
    private TracksServiceManager mTracksServiceManager;
    private TracksService mService;
    private TextView mTextTitle;
    private TextView mTextArtist;
    private ImageView mImageTrack;
    private EditText mEditTextSearch;
    private Button mButtonNextSong;
    private Button mButtonPreviousSong;
    private Button mButtonPlaySong;
    private ConstraintLayout mMiniPlayer;

    public static Intent getHomeIntent(Context context, List<Track> tracks) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BUNDLE_TRACKS, (ArrayList<? extends Parcelable>) tracks);
        Intent HomeScreen = new Intent(context,
                HomeActivity.class);
        HomeScreen.putExtras(bundle);
        return HomeScreen;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        connectService();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mService != null) {
            if (mService.getStateMedia() != ITracksPlayerManager.StatePlayerType.PLAYING) {
                mButtonPlaySong.setBackgroundResource(R.drawable.ic_play_button);
            } else {
                mButtonPlaySong.setBackgroundResource(R.drawable.ic_pause);
            }
        }
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        mService = ((TracksService.LocalBinder) iBinder).getService();
        mService.addControlListener(this);
        if (mService.getStateMiniPlayer() == true) {
            mMiniPlayer.setVisibility(View.VISIBLE);
        }
        if (mService.getStateMedia() != ITracksPlayerManager.StatePlayerType.PLAYING) {
            mButtonPlaySong.setBackgroundResource(R.drawable.ic_play_button);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    @Override
    public void onClickTrack(List<Track> tracks, int i) {
        mTrackPosition = i;
        mService.setTracks(tracks);
        mService.setPositionTrack(mTrackPosition);
        mService.play(mTrackPosition);
        if (mMiniPlayer.getVisibility() == View.GONE) {
            mMiniPlayer.setVisibility(View.VISIBLE);
            startActivity(PlayActivity.getPlayIntent(this));
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
                setStateTrack();
                break;
            case R.id.button_next:
                mService.next();
                mButtonPlaySong.setBackgroundResource(R.drawable.ic_pause);
                break;
            default:
                startActivity(SearchActivity.getSearchIntent(this));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mService != null) {
            if (mService.getStateMiniPlayer()) {
                mMiniPlayer.setVisibility(View.VISIBLE);
                MarginUtil.getInstance().setMargins(mViewPagerMusic, MARGIN_DEFAULT,
                        MARGIN_DEFAULT, MARGIN_DEFAULT, MARGIN_BOTTOM);
            }
        }
    }

    private void initUI() {
        Bundle bundle = getIntent().getExtras();
        mMiniPlayer = findViewById(R.id.mini_player);
        mViewPagerMusic = findViewById(R.id.view_pager);
        mViewPagerMusic.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(),
                bundle, this));
        mTabLayout = findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPagerMusic);
        mImageTrack = findViewById(R.id.image_avatar);
        mTextTitle = findViewById(R.id.text_song_name);
        mTextArtist = findViewById(R.id.text_singer_name);
        mButtonNextSong = findViewById(R.id.button_next);
        mButtonPlaySong = findViewById(R.id.button_play);
        mButtonPreviousSong = findViewById(R.id.button_back);
        mEditTextSearch = findViewById(R.id.edit_search);
        setOnClickListener();
    }

    private void setOnClickListener() {
        mImageTrack.setOnClickListener(this);
        mButtonNextSong.setOnClickListener(this);
        mButtonPreviousSong.setOnClickListener(this);
        mButtonPlaySong.setOnClickListener(this);
        mEditTextSearch.setOnClickListener(this);
    }

    private void connectService() {
        if (mService != null) {
            if (mService.getStateMiniPlayer()) mMiniPlayer.setVisibility(View.VISIBLE);
        }
        ServiceConnection connection = this;
        Intent intent = new Intent(this, TracksService.class);
        mTracksServiceManager = new TracksServiceManager(this, intent, connection,
                Context.BIND_AUTO_CREATE);
        mTracksServiceManager.bindService();
    }

    private void setStateTrack() {
        if (mService.getStateMedia() == ITracksPlayerManager.StatePlayerType.PLAYING) {
            mService.pause();
            mButtonPlaySong.setBackgroundResource(R.drawable.ic_play_button);
        } else {
            mService.start();
            mButtonPlaySong.setBackgroundResource(R.drawable.ic_pause);
        }
    }

    private void setItemMiniPlayer(Track track) {
        Glide.with(getApplicationContext())
                .load(track.getArtWorkUrl())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.soundcloud)
                        .error(R.drawable.soundcloud))
                .into(mImageTrack);
        mTextTitle.setText(track.getTitle());
        mTextArtist.setText(track.getUserName());
    }
}
