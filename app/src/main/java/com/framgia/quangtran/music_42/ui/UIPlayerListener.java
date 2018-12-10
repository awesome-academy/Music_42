package com.framgia.quangtran.music_42.ui;

import com.framgia.quangtran.music_42.data.model.Track;
import com.framgia.quangtran.music_42.mediaplayer.ITracksPlayerManager;
import com.framgia.quangtran.music_42.mediaplayer.TracksPlayerSetting;

public interface UIPlayerListener {
    interface ControlListener {

        void notifyShuffleChanged(@TracksPlayerSetting.ShuffleType int shuffleType);

        void notifyLoopChanged(@TracksPlayerSetting.LoopType int loopType);

        void notifyStateChanged(Track track, @ITracksPlayerManager.StatePlayerType int statusType);
    }
}
