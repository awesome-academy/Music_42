package com.framgia.quangtran.music_42.ui.splash;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.GenreKey;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.local.TrackLocalDataSource;
import com.framgia.quangtran.music_42.data.source.remote.TrackRemoteDataSource;
import com.framgia.quangtran.music_42.ui.home.HomeActivity;
import com.framgia.quangtran.music_42.util.StringUtil;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {
    private static final int READ_EXTERNAL_STORAGE = 1;
    private static final int DELAY_MILLIS = 2000;
    private static final int OFFSET = 1;
    public static final int LIMIT = 8;
    private String mApi;
    private Handler mHandler;
    private SplashContract.Presenter mSlashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initUI();
        StringUtil util = new StringUtil();
        mApi = StringUtil.genreApi(GenreKey.ALL_MUSIC, LIMIT, OFFSET);
        checkPermission(mApi);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.status_bar));
        }
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        TrackRepository repository = TrackRepository.getInstance(TrackRemoteDataSource
                .getInstance(), TrackLocalDataSource.getInstance(getApplicationContext(),contentResolver));
        mSlashPresenter = new SplashPresenter(repository);
        mSlashPresenter.setView(this);
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
        if (isNetworkConnected()) {
            mSlashPresenter.loadOnlineMusic(api);
        } else {
            DelayLoadData();
            return;
        }
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
            }
        }
        if (isNetworkConnected()) {
            mSlashPresenter.loadOnlineMusic(mApi);
        }
        else {
            DelayLoadData();
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() == null ? false : true;
    }

    @Override
    public void onSuccess(List<Track> tracks) {
        startActivity(HomeActivity.getHomeIntent(SplashActivity.this, tracks));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onFailure(String message) {
        DelayLoadData();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
