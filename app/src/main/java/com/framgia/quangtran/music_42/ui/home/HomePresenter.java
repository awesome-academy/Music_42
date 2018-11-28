package com.framgia.quangtran.music_42.ui.home;

import com.framgia.quangtran.music_42.data.model.Genre;
import com.framgia.quangtran.music_42.data.repository.GenreRepository;
import com.framgia.quangtran.music_42.data.source.GenreDataSource;

import java.util.List;

public class HomePresenter implements HomeContract.Presenter {
    private GenreRepository mRepository;
    private HomeContract.View mView;

    public HomePresenter(GenreRepository repository, HomeContract.View view) {
        this.mRepository = repository;
        this.mView = view;
    }

    @Override
    public void getGenres() {
        mRepository.getGenres(new GenreDataSource.DataCallback<Genre>() {
            @Override
            public void onSuccess(List<Genre> data) {
                mView.onSuccess(data);
            }

            @Override
            public void onFailure(String message) {
                mView.onFailure(message);
            }
        });
    }
}
