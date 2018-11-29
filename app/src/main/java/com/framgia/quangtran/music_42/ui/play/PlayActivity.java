package com.framgia.quangtran.music_42.ui.play;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class PlayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Intent getPlayIntent(Context context) {
        Intent intent = new Intent(context, PlayActivity.class);
        return intent;
    }
}
