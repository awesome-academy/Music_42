package com.framgia.quangtran.music_42.data.model;

import java.io.Serializable;

public class Track implements Serializable {
    private long mId;
    private String mTitle;
    private String mDataSource;
    private String mArtWorkUrl;
    private String mGenre;
    private String mDescription;
    private String mUserName;
    private int mDuration;
    private String mDownLoadUrl;
    private boolean mIsDownload;
    private boolean mIsOffline;

    public Track() {
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getDataSource() {
        return mDataSource;
    }

    public void setDataSource(String dataSource) {
        mDataSource = dataSource;
    }

    public String getArtWorkUrl() {
        return mArtWorkUrl;
    }

    public void setArtWorkUrl(String artWorkUrl) {
        mArtWorkUrl = artWorkUrl;
    }

    public String getGenre() {
        return mGenre;
    }

    public void setGenre(String genre) {
        mGenre = genre;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public String getDownLoadUrl() {
        return mDownLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        mDownLoadUrl = downLoadUrl;
    }

    public boolean isDownload() {
        return mIsDownload;
    }

    public void setDownload(boolean download) {
        mIsDownload = download;
    }

    public boolean isOffline() {
        return mIsOffline;
    }

    public void setOffline(boolean offline) {
        mIsOffline = offline;
    }
}
