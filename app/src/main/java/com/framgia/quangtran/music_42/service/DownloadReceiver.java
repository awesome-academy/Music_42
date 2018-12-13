package com.framgia.quangtran.music_42.service;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.framgia.quangtran.music_42.util.StringUtil;

public class DownloadReceiver extends ResultReceiver {
    private DownloadListener mDownloadListener;

    public DownloadReceiver(DownloadListener listener, Handler handler) {
        super(handler);
        mDownloadListener = listener;
    }

    @Override
    protected void onReceiveResult(@DownloadListener.DownloadRequest int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        switch (resultCode) {
            case DownloadListener.DownloadRequest.PREPARE:
                String title = resultData.getString(StringUtil.EXTRA_TITLE);
                mDownloadListener.onPrepare(title);
                break;
            case DownloadListener.DownloadRequest.UPDATE:
                int progress = resultData.getInt(StringUtil.EXTRA_PROGRESS);
                mDownloadListener.onDownloading(progress);
                break;
            case DownloadListener.DownloadRequest.FINISH:
                mDownloadListener.onSuccess();
                break;
            case DownloadListener.DownloadRequest.ERROR:
                String message = resultData.getString(StringUtil.EXTRA_ERROR);
                mDownloadListener.onFailure(message);
                break;
            default:
                break;
        }
    }
}
