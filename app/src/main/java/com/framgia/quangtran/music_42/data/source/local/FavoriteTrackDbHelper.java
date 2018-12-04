package com.framgia.quangtran.music_42.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.source.TrackDataSource;
import com.framgia.quangtran.music_42.data.source.local.contact.FavoriteReaderContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoriteTrackDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";
    private static final String LIKE_ARG = " LIKE ?";
    private static final String QUESTION_ARG = " = ?";
    private static final String OPEN_PARENTHESIS = " (";
    private static final String CLOSE_PARENTHESIS = ")";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String TEXT_TYPE = " TEXT";
    public static final String COMMA = ",";
    public static final String DROP_IF_EXIST = "DROP TABLE IF EXISTS ";

    private static final String SQL_CREATE_FAVORITE_ENTRY =
            "CREATE TABLE " + FavoriteReaderContract.FavoriteEntry.TABLE_NAME + OPEN_PARENTHESIS +
                    FavoriteReaderContract.FavoriteEntry._ID + INTEGER_TYPE + PRIMARY_KEY + COMMA +
                    FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TRACK_ID + TEXT_TYPE + COMMA +
                    FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_ARTWORK_URL + TEXT_TYPE + COMMA +
                    FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_USER_NAME + TEXT_TYPE + COMMA +
                    FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA +
                    FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_DOWNLOADABLE + TEXT_TYPE + COMMA +
                    FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_DOWNLOAD_URL + TEXT_TYPE + COMMA +
                    FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_IS_OFFLINE + TEXT_TYPE +
                    CLOSE_PARENTHESIS;

    private static final String SQL_DELETE_FAVORITE_ENTRY =
            DROP_IF_EXIST + FavoriteReaderContract.FavoriteEntry.TABLE_NAME;

    public FavoriteTrackDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_ENTRY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_FAVORITE_ENTRY);
        onCreate(sqLiteDatabase);
    }

    public void putTrack(Track track, TrackDataSource.DataCallback callback) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TRACK_ID, track.getId());
        boolean trackType = track.isOffline() ? true : false;
        values.put(FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_IS_OFFLINE, trackType);
        values.put(FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TITLE, track.getTitle());
        values.put(FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_USER_NAME, track.getUserName());
        values.put(FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_ARTWORK_URL,
                track.getArtWorkUrl());
        values.put(FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_DOWNLOADABLE,
                track.isDownload());
        values.put(FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_DOWNLOAD_URL,
                track.getDownLoadUrl());
        long id = db.insert(FavoriteReaderContract.FavoriteEntry.TABLE_NAME,
                null, values);
        db.close();
        boolean result = id > 0 ? true : false;
        callback.onSuccess(Collections.singletonList(Boolean.valueOf(result)));
    }

    public void getTracks(TrackDataSource.DataCallback<Track> callback) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] projection = {
                FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TRACK_ID,
                FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_IS_OFFLINE,
                FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TITLE,
                FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_USER_NAME,
                FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_ARTWORK_URL,
                FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_DOWNLOADABLE,
                FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_DOWNLOAD_URL
        };
        Cursor cursor = sqLiteDatabase.query(
                FavoriteReaderContract.FavoriteEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);
        List<Track> tracks = new ArrayList<>();
        while (cursor.moveToNext()) {
            String id = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TRACK_ID));
            String isOffline = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_IS_OFFLINE));
            String title = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TITLE));
            String artist = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_USER_NAME));
            String artworkUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_ARTWORK_URL));
            String downloadable = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_DOWNLOADABLE));
            String downloadUrl = cursor.getString(
                    cursor.getColumnIndexOrThrow(
                            FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_DOWNLOAD_URL));
            Track track = new Track();
            track.setId(Long.valueOf(id));
            track.setTitle(title);
            track.setUserName(artist);
            track.setTitle(title);
            track.setArtWorkUrl(artworkUrl);
            track.setDownload(Boolean.valueOf(downloadable));
            track.setDownLoadUrl(downloadUrl);
            track.setOffline(Boolean.valueOf(isOffline));
            tracks.add(track);
        }
        sqLiteDatabase.close();
        callback.onSuccess(tracks);
    }

    public void deleteTrack(Track track, TrackDataSource.DataCallback<Boolean> callback) {
        long trackId = track.getId();
        SQLiteDatabase db = getReadableDatabase();
        String selection = FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TRACK_ID + LIKE_ARG;
        String[] selectionArgs = {String.valueOf(trackId)};
        int deletedRows = db.delete(
                FavoriteReaderContract.FavoriteEntry.TABLE_NAME,
                selection,
                selectionArgs);
        db.close();
        boolean result = deletedRows > 0 ? true : false;
        callback.onSuccess(Collections.singletonList(Boolean.valueOf(result)));
    }

    public boolean canAddTrack(Track track) {
        long trackId = track.getId();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String[] projection = {
                FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TRACK_ID,
        };
        String selection = FavoriteReaderContract.FavoriteEntry.COLUMN_NAME_TRACK_ID + QUESTION_ARG;
        String[] selectionArgs = {String.valueOf(trackId)};
        Cursor cursor = sqLiteDatabase.query(
                FavoriteReaderContract.FavoriteEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null);
        return (cursor.getCount() <= 0);
    }
}
