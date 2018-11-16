package com.framgia.quangtran.music_42.util;

import com.framgia.quangtran.music_42.BuildConfig;
import com.framgia.quangtran.music_42.data.model.GenreKey;
import com.framgia.quangtran.music_42.ui.Contracts;

public class StringUtil {
    public static String append(String... strings) {
        StringBuilder builder = new StringBuilder();
        for (String string : strings) {
            builder.append(string);
        }
        return builder.toString();
    }

    public static String GenreApi(@GenreKey String genreKey, int offset) {
        return StringUtil.append(Contracts.BASE_URL_GENRES, genreKey,
                Contracts.CLIENT_ID, BuildConfig.API_KEY,
                Contracts.PARAMETER_LIMIT, String.valueOf(Contracts.LIMIT),
                Contracts.PARAMETER_OFFSET, String.valueOf(offset));
    }
}
