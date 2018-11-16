package com.framgia.quangtran.music_42.data.model;

public class Genre {
    private String mName;
    private String mKey;
    private int mImageUrl;

    public Genre(@GenreName String genreName, @GenreKey String key, int imageUrl) {
        mName = genreName;
        mKey = key;
        mImageUrl = imageUrl;
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

    public int getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(int imageUrl) {
        mImageUrl = imageUrl;
    }
}
