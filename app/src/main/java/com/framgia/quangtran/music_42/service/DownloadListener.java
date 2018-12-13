package com.framgia.quangtran.music_42.service;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface DownloadListener {
    void onPrepare(String title);

    void onDownloading(int progress);

    void onSuccess();

    void onFailure(String message);

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            DownloadRequest.PREPARE, DownloadRequest.UPDATE,
            DownloadRequest.FINISH, DownloadRequest.ERROR
    })
    @interface DownloadRequest {
        int PREPARE = 101;
        int UPDATE = 102;
        int FINISH = 103;
        int ERROR = 104;
    }
}
