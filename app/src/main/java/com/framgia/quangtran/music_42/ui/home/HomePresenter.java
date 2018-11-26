package com.framgia.quangtran.music_42.ui.home;

import com.framgia.quangtran.music_42.data.model.Genre;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private TrackRepository mRepository;

    public HomePresenter(TrackRepository repository) {
        this.mRepository = repository;
    }

    @Override
    public List<Genre> getGenres() {
        List<Genre> genres = mRepository.genreRepository();
        return genres;
    }
}
