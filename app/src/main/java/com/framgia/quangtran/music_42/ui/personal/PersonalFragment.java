package com.framgia.quangtran.music_42.ui.personal;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.local.TrackLocalDataSource;
import com.framgia.quangtran.music_42.data.source.remote.TrackRemoteDataSource;

import java.util.List;

public class PersonalFragment extends Fragment implements PersonalContract.View {
    private PersonalPresenter mPresenter;
    private ContentResolver mResolver;

    public static PersonalFragment newInstance() {
        PersonalFragment personalFragment = new PersonalFragment();
        return personalFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initUI();
        return inflater.inflate(R.layout.fragment_personal, container, false);
    }

    private void initUI() {
        TrackRepository repository = TrackRepository.getInstance(TrackRemoteDataSource
                .getInstance(), TrackLocalDataSource.getInstance());
        mResolver = getActivity().getContentResolver();
        mPresenter = new PersonalPresenter(repository, mResolver);
        mPresenter.setView(this);
        mPresenter.loadOfflineMusic();
    }

    @Override
    public void onSuccess(List<Track> tracks) {
    }

    @Override
    public void onFailure(String message) {

    }
}
