package com.framgia.quangtran.music_42.ui.personal;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.framgia.quangtran.music_42.R;
import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.ui.storage.StorageActivity;
import com.framgia.quangtran.music_42.ui.storage.contract.StorageStyle;

import java.util.List;

public class PersonalFragment extends Fragment implements PersonalContract.View, View.OnClickListener {
    private LinearLayout mLinearLocal;
    private LinearLayout mLinearDownload;
    private LinearLayout mLinearFavorite;


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

    private void initUI(View view) {
        mLinearLocal = view.findViewById(R.id.linear_local);
        mLinearDownload = view.findViewById(R.id.linear_download);
        mLinearFavorite = view.findViewById(R.id.linear_favorite);
        mLinearLocal.setOnClickListener(this);
        mLinearDownload.setOnClickListener(this);
        mLinearFavorite.setOnClickListener(this);
    }

    @Override
    public void onSuccess(List<Track> tracks) {
    }

    @Override
    public void onFailure(String message) {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linear_local:
                startActivity(StorageActivity.getStorageIntent(getContext(),
                        StorageStyle.StorageKey.LOCAL));
                break;
            case R.id.linear_download:
                startActivity(StorageActivity.getStorageIntent(getContext(),
                        StorageStyle.StorageKey.DOWNLOAD));
                break;
            default:
                startActivity(StorageActivity.getStorageIntent(getContext(),
                        StorageStyle.StorageKey.FAVORITE));
                break;
        }
    }
}
