package com.framgia.quangtran.music_42.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.mediaplayer.ITracksPlayerManager;
import com.framgia.quangtran.music_42.mediaplayer.TracksPlayerManager;
import com.framgia.quangtran.music_42.mediaplayer.TracksPlayerSetting;
import com.framgia.quangtran.music_42.ui.UIPlayerListener;
import com.framgia.quangtran.music_42.ui.play.PlayActivity;

import java.util.List;

public class TracksITracksService extends Service implements ITracksService {
    private static final int REQUEST_CODE = 1000;
    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    private static final String ACTION_PLAY = "ACTION_PLAY";
    private static final String ACTION_PAUSE = "ACTION_PAUSE";
    private static final String ACTION_NEXT = "ACTION_NEXT";
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private final IBinder mIBinder = new LocalBinder();
    private TracksPlayerManager mMediaPlayerManager;
    private NotificationManager mNotificationManager;
    private Notification mNotificationUI;
    private NotificationTarget mNotificationTarget;
    private RemoteViews mRemoteViews;
    private Intent mIntentNotification;
    private Intent mIntentRemoteView;
    private PendingIntent mPendingIntentNotification;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIBinder;
    }

    public class TrackBinder extends Binder {
        public TracksITracksService getService() {
            return TracksITracksService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMediaPlayerManager = TracksPlayerManager.getInstance(this);
        mMediaPlayerManager.initMediaPlayer();
        initNotification();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void start() {
        mMediaPlayerManager.start();
    }

    @Override
    public void pause() {
        mMediaPlayerManager.pause();
    }

    @Override
    public void next() {
        mMediaPlayerManager.next();
    }

    @Override
    public void previous() {
        mMediaPlayerManager.previous();
    }

    public void setShuffle(@TracksPlayerSetting.ShuffleType int shuffleType) {
        mMediaPlayerManager.setShuffleType(shuffleType);
    }

    @ITracksPlayerManager.StatePlayerType
    public int getShuffle() {
        return mMediaPlayerManager.getShuffleType();
    }

    public void setLoop(@TracksPlayerSetting.LoopType int loopType) {
        mMediaPlayerManager.setLoopType(loopType);
    }

    @TracksPlayerSetting.LoopType
    public int getLoop() {
        return mMediaPlayerManager.getLoopType();
    }

    @Override
    public void seekTo(int msec) {
        mMediaPlayerManager.seekTo(msec);
    }

    @Override
    public long getCurrentPosition() {
        return mMediaPlayerManager.getCurrentPosition();
    }

    @Override
    public long getDuration() {
        return mMediaPlayerManager.getDuration();
    }

    @Override
    public void setPositionTrack(int i) {
        mMediaPlayerManager.setPositionTrack(i);
    }

    @Override
    public int getPositionTrack() {
        return mMediaPlayerManager.getPositionTrack();
    }

    @Override
    public Track getTrack() {
        return mMediaPlayerManager.getTrack();
    }

    @Override
    public void setTracks(List<Track> tracks) {
        mMediaPlayerManager.setTracks(tracks);
    }

    @Override
    public List<Track> getTracks() {
        return mMediaPlayerManager.getTracks();
    }

    @Override
    public void setTrackInfo(TextView title, TextView artist) {
        mMediaPlayerManager.setTrackInfo(title, artist);
    }

    @Override
    public void setPlayTrackInfo(TextView title, TextView artist, ImageView imageBackGround, ImageView imageArtwork) {
        mMediaPlayerManager.setPlayTrackInfo(title, artist, imageBackGround, imageArtwork);
    }

    @Override
    public int getStateMedia() {
        return mMediaPlayerManager.getState();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class LocalBinder extends Binder {
        public TracksITracksService getService() {
            return TracksITracksService.this;
        }
    }

    public static Intent getMyServiceIntent(Context context) {
        Intent intent = new Intent(context, TracksITracksService.class);
        return intent;
    }

    public void play(int position) {
        mMediaPlayerManager.initMediaPlayer();
        mMediaPlayerManager.setPositionTrack(position);
        mMediaPlayerManager.initPlay(position);
        createNotification();
    }

    public void addControlListener(UIPlayerListener.ControlListener controlListener) {
        mMediaPlayerManager.addControlListener(controlListener);
    }

    public void removeControlListener(UIPlayerListener.ControlListener controlListener) {
        mMediaPlayerManager.removeControlListener(controlListener);
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mIntentRemoteView = new Intent(this, TracksITracksService.class);
        mIntentNotification = new Intent(this, PlayActivity.class);
        mIntentNotification.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntentNotification = PendingIntent.getActivity(this, REQUEST_CODE,
                mIntentNotification, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification);
    }

    private void createNotification() {
        setPreviousRemoteView(mIntentRemoteView);
        setPauseRemoteView(mIntentRemoteView);
        setNextRemoteView(mIntentRemoteView);
        mNotificationUI = buildNotification();
        mNotificationTarget = buildNotificationTarget();
        updateDescriptionNotification(mMediaPlayerManager.getTrack());
    }

    private void setPreviousRemoteView(Intent intent) {
        intent.setAction(ACTION_PREVIOUS);
        PendingIntent previousPendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.image_previous, previousPendingIntent);
    }

    private void setPauseRemoteView(Intent intent) {
        intent.setAction(ACTION_PAUSE);
        PendingIntent pausePendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.image_play, pausePendingIntent);
        mRemoteViews.setImageViewResource(R.id.image_play, R.drawable.ic_pause);
    }

    private void setNextRemoteView(Intent intent) {
        intent.setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(this, REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mRemoteViews.setOnClickPendingIntent(R.id.button_next, nextPendingIntent);
    }

    private Notification buildNotification() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentIntent(mPendingIntentNotification)
                .setSmallIcon(R.drawable.soundcloud)
                .setContent(mRemoteViews)
                .build();
    }

    private NotificationTarget buildNotificationTarget() {
        return new NotificationTarget(this, R.id.image_track, mRemoteViews, mNotificationUI,
                NOTIFICATION_ID);
    }

    private void updateDescriptionNotification(Track track) {
        Glide.with(this)
                .asBitmap()
                .load(track.getArtWorkUrl())
                .into(mNotificationTarget);
        startForeground(NOTIFICATION_ID, mNotificationUI);
    }
}