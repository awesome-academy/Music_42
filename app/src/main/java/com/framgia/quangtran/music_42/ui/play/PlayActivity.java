package com.framgia.quangtran.music_42.ui.play;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.framgia.quangtran.music_42.R;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, ServiceConnection {
    private ImageView mImageShuffle;
    private ImageView mImagePrevious;
    private ImageView mImageNext;
    private ImageView mImagePlay;
    private ImageView mImageLoop;
    private SeekBar mSeekBar;
    private TextView mCurrentTime;
    private TextView mTotalTime;
    private boolean mIsBoundService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_music);
        initUI();
    }

    public static Intent getPlayIntent(Context context) {
        Intent intent = new Intent(context,PlayActivity.class);
        return intent;
    }

    private void initUI() {
        mImageShuffle = findViewById(R.id.image_shuffle);
        mImagePrevious = findViewById(R.id.image_previous);
        mImagePlay = findViewById(R.id.image_play);
        mImageNext = findViewById(R.id.image_next);
        mImageLoop = findViewById(R.id.image_loop);
        mSeekBar = findViewById(R.id.seek_bar_track);
        mCurrentTime = findViewById(R.id.text_current_time);
        mTotalTime = findViewById(R.id.text_total_time);
        setListener();
    }

    public void setListener() {
        mImageShuffle.setOnClickListener(this);
        mImagePrevious.setOnClickListener(this);
        mImagePlay.setOnClickListener(this);
        mImageNext.setOnClickListener(this);
        mImageLoop.setOnClickListener(this);
        mCurrentTime.setOnClickListener(this);
        mTotalTime.setOnClickListener(this);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        mIsBoundService = false;
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
