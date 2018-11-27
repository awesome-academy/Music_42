package com.framgia.quangtran.music_42.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {
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
    private boolean mFavorite = false;

    public Track() {
    }

    protected Track(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mDataSource = in.readString();
        mArtWorkUrl = in.readString();
        mGenre = in.readString();
        mDescription = in.readString();
        mUserName = in.readString();
        mDuration = in.readInt();
        mDownLoadUrl = in.readString();
        mIsDownload = in.readByte() != 0;
        mIsOffline = in.readByte() != 0;
        mFavorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mDataSource);
        parcel.writeString(mArtWorkUrl);
        parcel.writeString(mGenre);
        parcel.writeString(mDescription);
        parcel.writeString(mUserName);
        parcel.writeInt(mDuration);
        parcel.writeString(mDownLoadUrl);
        parcel.writeByte((byte) (mIsDownload ? 1 : 0));
        parcel.writeByte((byte) (mIsOffline ? 1 : 0));
        parcel.writeByte((byte) (mFavorite ? 1 : 0));
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };

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


    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static class JSONKey {
        public static final String ARTWORK_URL = "artwork_url";
        public static final String ID = "id";
        public static final String KEY_USER = "user";
        public static final String KEY_USER_NAME = "username";
        public static final String TITLE = "title";
        public static final String DOWNLOADABLE = "downloadable";
        public static final String DOWNLOAD_URL = "download_url";
        public static final String TRACK = "track";
        public static final String COLLECTION = "collection";
    }
}
