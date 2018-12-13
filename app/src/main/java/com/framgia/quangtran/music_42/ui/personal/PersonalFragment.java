package com.framgia.quangtran.music_42.ui.personal;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.data.repository.TrackRepository;
import com.framgia.quangtran.music_42.data.source.local.TrackLocalDataSource;
import com.framgia.quangtran.music_42.data.source.remote.TrackRemoteDataSource;
import com.framgia.quangtran.music_42.ui.home.adapters.TodayAdapter;
import com.framgia.quangtran.music_42.ui.storage.StorageActivity;
import com.framgia.quangtran.music_42.ui.storage.contract.StorageStyle;
import com.framgia.quangtran.music_42.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PersonalFragment extends Fragment implements PersonalContract.View,
        View.OnClickListener, TodayAdapter.ClickTrackElement {
    private static final int READ_EXTERNAL_STORAGE = 1;
    private boolean isPermission = false;
    private LinearLayout mLinearLocal;
    private LinearLayout mLinearDownload;
    private LinearLayout mLinearFavorite;
    private RecyclerView mRecyclerRecent;
    private PersonalContract.Presenter mPresenter;
    private List<Track> mTracks;
    private TodayAdapter mTodayAdapter;

    public static PersonalFragment newInstance() {
        PersonalFragment personalFragment = new PersonalFragment();
        return personalFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal, container, false);
        initUI(view);
        return view;
    }

    @Override
    public void onSuccessId(List<Long> idTracks) {
        //init recycler adapter
        if (getActivity() != null) {
            for (int i = 0; i < idTracks.size(); i++) {
                String api = StringUtil.initDetailApi(idTracks.get(i).longValue());
                mPresenter.loadRecentTracks(api);
            }
        }
    }

    @Override
    public void onRecentTracksSuccess(List<Track> Tracks) {
        mTracks.addAll(Tracks);
        mTodayAdapter.notifyDataSetChanged();

    }

    @Override
    public void onFailure(String message) {
    }

    @Override
    public void onClick(View view) {
        checkPermission();
        switch (view.getId()) {
            case R.id.linear_local:
                if (isPermission == true) {
                    startActivity(StorageActivity.getStorageIntent(getContext(),
                            StorageStyle.StorageKey.LOCAL));
                }
                break;

            case R.id.linear_download:
                if (isPermission == true) {
                    startActivity(StorageActivity.getStorageIntent(getContext(),
                            StorageStyle.StorageKey.DOWNLOAD));
                }
                break;
            case R.id.linear_favorite:
                startActivity(StorageActivity.getStorageIntent(getContext(),
                        StorageStyle.StorageKey.FAVORITE));
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager
                    .PERMISSION_DENIED) {
                isPermission = true;
            } else {
                checkPermission();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initRepository();
        initRecycler();
        mTodayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClickTrack(List<Track> tracks, int i) {
    }

    private void initUI(View view) {
        mLinearLocal = view.findViewById(R.id.linear_local);
        mLinearDownload = view.findViewById(R.id.linear_download);
        mLinearFavorite = view.findViewById(R.id.linear_favorite);
        mRecyclerRecent = view.findViewById(R.id.recycler_recent);
        mLinearLocal.setOnClickListener(this);
        mLinearDownload.setOnClickListener(this);
        mLinearFavorite.setOnClickListener(this);
    }

    private void initRepository() {
        TrackRepository repository = TrackRepository.getInstance(TrackRemoteDataSource
                .getInstance(), TrackLocalDataSource.getInstance(getContext(),
                getActivity().getContentResolver()));
        mPresenter = new PersonalPresenter(getActivity().getApplicationContext(), repository);
        mPresenter.setView(this);
        mPresenter.getRecentTracks();
    }

    private void initRecycler() {
        mTracks = new ArrayList<>();
        mRecyclerRecent.setLayoutManager(new LinearLayoutManager(getContext()));
        mTodayAdapter = new TodayAdapter(mTracks, this);
        mRecyclerRecent.setAdapter(mTodayAdapter);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
        } else {
            isPermission = true;
        }
    }
}
