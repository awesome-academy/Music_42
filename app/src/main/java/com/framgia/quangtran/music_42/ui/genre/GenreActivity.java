package com.framgia.quangtran.music_42.ui.genre;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Genre;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.local.TrackLocalDataSource;
import com.framgia.quangtran.music_42.data.source.remote.TrackRemoteDataSource;
import com.framgia.quangtran.music_42.util.StringUtil;

import java.util.List;

public class GenreActivity extends AppCompatActivity implements GenreContract.View {
    private static final int OFFSET = 1;
    public static final int LIMIT = 20;
    private static final String BUNDLE_GENRE = "com.framgia.quangtran.music_42.ui.genre.BUNDLE_GENRE";
    private GenrePresenter mGenrePresenter;
    private ContentResolver mContentResolverCursor;
    private RecyclerView mRecyclerGenres;
    private String mApi;

    public static Intent getGenreIntent(Context context, Genre genre) {
        Intent intent = new Intent(context, GenreActivity.class);
        intent.putExtra(BUNDLE_GENRE, genre);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        initUI();
        loadMusic();
    }


    private void loadMusic() {
        Genre genre = getIntent().getParcelableExtra(BUNDLE_GENRE);
        mApi = StringUtil.genreApi(genre.getKey(), LIMIT, OFFSET);
        mGenrePresenter.getGenres(mApi);
    }

    private void initUI() {
        mRecyclerGenres = findViewById(R.id.recycler_genre);
        mContentResolverCursor = getApplicationContext().getContentResolver();
        TrackRepository repository = TrackRepository.getInstance(TrackRemoteDataSource
                .getInstance(), TrackLocalDataSource.getInstance(mContentResolverCursor));
        mGenrePresenter = new GenrePresenter(repository);
        mGenrePresenter.setView(this);
        mRecyclerGenres.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onSuccess(List<Track> tracks) {
        GenreAdapter genreAdapter = new GenreAdapter(tracks);
        mRecyclerGenres.setAdapter(genreAdapter);
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
