package com.framgia.quangtran.music_42.data.model;

public class Genre {
    private String mName;
    private String mKey;
    private String mImageUrl;

    public Genre(String genreName, String key, String ImageUrl) {
        mName = genreName;
        mKey = key;
        mImageUrl = ImageUrl;
    }

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

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String ImageUrl) {
        mImageUrl = ImageUrl;
    }
}
