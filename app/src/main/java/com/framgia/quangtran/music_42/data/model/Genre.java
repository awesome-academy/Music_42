package com.framgia.quangtran.music_42.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable {
    private String mName;
    private String mKey;
    private int mImageUrl;

    public Genre(@GenreName String genreName, @GenreKey String key, int imageUrl) {
        mName = genreName;
        mKey = key;
        mImageUrl = imageUrl;
    }

    protected Genre(Parcel in) {
        mName = in.readString();
        mKey = in.readString();
        mImageUrl = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mKey);
        parcel.writeInt(mImageUrl);
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public int getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(int imageUrl) {
        mImageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
