package com.framgia.quangtran.music_42.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Genre;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.ui.genre.GenreActivity;
import com.framgia.quangtran.music_42.ui.home.adapters.GenresAdapter;
import com.framgia.quangtran.music_42.ui.home.adapters.TodayAdapter;

import java.util.List;

public class HomeFragment extends Fragment implements GenresAdapter.GenreClickListener {
    private static final String BUNDLE_GENRE = "genre";
    private static List<Track> mTracks;
    private HomePresenter mHomePresenter;

    public static HomeFragment newInstance(List<Track> tracks) {
        HomeFragment homeFragment = new HomeFragment();
        mTracks = tracks;
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if (mTracks != null) {
            initView(view, mTracks);
        }
        return view;
    }

    void initView(View view, List<Track> tracks) {
        mHomePresenter = new HomePresenter();
        RecyclerView mRecyclerToday = view.findViewById(R.id.recycler_recent);
        mRecyclerToday.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        TodayAdapter todayAdapter = new TodayAdapter(tracks);
        mRecyclerToday.setAdapter(todayAdapter);
        RecyclerView mRecyclerGenres = view.findViewById(R.id.recycler_genres);
        mRecyclerGenres.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        GenresAdapter genresAdapter = new GenresAdapter(this, mHomePresenter.getGenres());
        mRecyclerGenres.setAdapter(genresAdapter);
    }

    @Override
    public void onItemClickGenre(Genre genre) {
        startActivity(getGenreIntent(getActivity(), genre));
    }

    public static Intent getGenreIntent(Context context, Genre genre) {
        Intent intent = new Intent(context, GenreActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_GENRE, genre);
        intent.putExtra(BUNDLE_GENRE, genre);
        return intent;
    }
}
