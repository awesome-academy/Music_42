package com.framgia.quangtran.music_42.ui.home;

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
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.local.TrackLocalDataSource;
import com.framgia.quangtran.music_42.data.source.remote.TrackRemoteDataSource;
import com.framgia.quangtran.music_42.ui.genre.GenreActivity;
import com.framgia.quangtran.music_42.ui.home.adapters.GenresAdapter;
import com.framgia.quangtran.music_42.ui.home.adapters.TodayAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements GenresAdapter.GenreClickListener,HomeContract.View {
    private static final String ARGUMENT_TRACKS = "ARGUMENT_TRACKS";
    private List<Track> mTracks;
    private HomeContract.Presenter mHomePresenter;

    public static HomeFragment newInstance(ArrayList<Track> tracks) {
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ARGUMENT_TRACKS, tracks);
        homeFragment.setArguments(bundle);
        return homeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mTracks = getArguments().getParcelableArrayList(ARGUMENT_TRACKS);
        if (mTracks != null) {
            initView(view, mTracks);
        }
        return view;
    }

    void initView(View view, List<Track> tracks) {
        TrackRepository repository = TrackRepository.getInstance(TrackRemoteDataSource
                .getInstance(), TrackLocalDataSource.getInstance());
        mHomePresenter = new HomePresenter(repository);
        RecyclerView mRecyclerToday = view.findViewById(R.id.recycler_recent);
        RecyclerView mRecyclerGenres = view.findViewById(R.id.recycler_genres);
        mRecyclerToday.setLayoutManager(new LinearLayoutManager(getContext()));
        TodayAdapter todayAdapter = new TodayAdapter(tracks);
        mRecyclerToday.setAdapter(todayAdapter);
        mRecyclerGenres.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        GenresAdapter genresAdapter = new GenresAdapter( mHomePresenter.getGenres(),this);
        mRecyclerGenres.setAdapter(genresAdapter);
    }

    @Override
    public void onItemClickGenre(Genre genre) {
        startActivity(GenreActivity.getGenreIntent(getActivity(), genre));
    }
}
