package com.framgia.quangtran.music_42.ui.splashscreen;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.GenreKey;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.local.TrackLocalDataSource;
import com.framgia.quangtran.music_42.data.source.remote.TrackRemoteDataSource;
import com.framgia.quangtran.music_42.ui.homescreen.HomeActivity;
import com.framgia.quangtran.music_42.util.StringUtil;

import java.io.Serializable;
import java.util.List;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private static final int READ_EXTERNAL_STORAGE = 1;
    private static final int DELAY_MILLIS = 2000;
    private static final int OFFSET = 1;
    public static final int LIMIT = 8;
    private static final String TRACKS = "tracks";
    private String mApi;
    private Handler mHandler;
    private ContentResolver mContentResolverCursor;
    private SplashPresenter mSlashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        StringUtil util = new StringUtil();
        mApi = StringUtil.genreApi(GenreKey.ALL_MUSIC, LIMIT, OFFSET);
        checkPermission(mApi);
    }

    private void getData() {
    }

    void initUI() {
        TrackRepository repository = TrackRepository.getInstance(TrackRemoteDataSource
                .getInstance(), TrackLocalDataSource.getInstance());
        mContentResolverCursor = getApplicationContext().getContentResolver();
        mSlashPresenter = new SplashPresenter(repository, this);
    }

    private void checkPermission(String api) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager
                .PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                    , READ_EXTERNAL_STORAGE);
            return;
        }
        mSlashPresenter.loadOnlineMusic(api);
    }

    public void DelayLoadData() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent HomeScreen = new Intent(SplashActivity.this,
                        HomeActivity.class);
                startActivity(HomeScreen);
                finish();
            }
        }, DELAY_MILLIS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager
                    .PERMISSION_DENIED) {
                mSlashPresenter.loadOnlineMusic(mApi);
                return;
            }
        }
        mSlashPresenter.loadOnlineMusic(mApi);
    }

    @Override
    public void onSuccess(List<Track> tracks) {
        startActivity(getProfileIntent(SplashActivity.this, tracks));
    }

    public static Intent getProfileIntent(Context context, List<Track> tracks) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TRACKS, (Serializable) tracks);
        Intent HomeScreen = new Intent(context,
                HomeActivity.class);
        HomeScreen.putExtras(bundle);
        return HomeScreen;
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
