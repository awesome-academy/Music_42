package com.framgia.quangtran.music_42.ui.homescreen.fragments;

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
import com.framgia.quangtran.music_42.data.model.GenreKey;
import com.framgia.quangtran.music_42.data.model.GenreName;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.ui.homescreen.adapters.GenresAdapter;
import com.framgia.quangtran.music_42.ui.homescreen.adapters.TodayAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String KEY_TRACK = "tracks";
    private RecyclerView mRecyclerToday;
    private RecyclerView mRecyclerGenres;

    public static HomeFragment getInstance(Bundle bundle){
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Bundle bundle = getArguments();
        initView(view, (List<Track>) bundle.getSerializable(KEY_TRACK));
        return view;
    }

    void initView(View view, List<Track> tracks) {
        mRecyclerToday = view.findViewById(R.id.recycler_recent);
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
        mRecyclerGenres = view.findViewById(R.id.recycler_genres);
        mRecyclerGenres.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        GenresAdapter genresAdapter = new GenresAdapter(Genres());
        mRecyclerGenres.setAdapter(genresAdapter);
    }

    public List<Genre> Genres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre(GenreName.ALL_MUSIC,
                GenreKey.ALL_MUSIC, R.drawable.soundcloud));
        genres.add(new Genre(GenreName.ALL_AUDIO,
                GenreKey.ALL_AUDIO, R.drawable.soundcloud));
        genres.add(new Genre(GenreName.ALTERNATIVE,
                GenreKey.ALTERNATIVE, R.drawable.soundcloud));
        genres.add(new Genre(GenreName.AMBIENT,
                GenreKey.AMBIENT, R.drawable.soundcloud));
        genres.add(new Genre(GenreName.CLASSICAL,
                GenreKey.CLASSICAL, R.drawable.soundcloud));
        genres.add(new Genre(GenreName.COUNTRY,
                GenreKey.COUNTRY, R.drawable.soundcloud));
        return genres;
    }
}
