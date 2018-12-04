package com.framgia.quangtran.music_42.ui;

import com.framgia.quangtran.music_42.mediaplayer.ITracksPlayerManager;
import com.framgia.quangtran.music_42.mediaplayer.TracksPlayerSetting;

public interface UIPlayerListener {
    interface ControlListener {

        void notifyShuffleChanged(@TracksPlayerSetting.ShuffleType int shuffleType);

        void notifyLoopChanged(@TracksPlayerSetting.LoopType int loopType);

        void notifyStateChanged(@ITracksPlayerManager.StatePlayerType int statusType);
    }
}
