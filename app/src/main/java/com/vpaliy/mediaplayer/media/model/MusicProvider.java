package com.vpaliy.mediaplayer.media.model;

import android.support.v4.media.MediaMetadataCompat;

public  class MusicProvider implements MediaProvider<MusicProvider.QueryArgs> {

    @Override
    public int count() {
        return 0;
    }

    @Override
    public MediaMetadataCompat byId(String mediaId) {
        return null;
    }

    @Override
    public MediaMetadataCompat at(int index) {
        return null;
    }

    @Override
    public MediaMetadataCompat search(QueryArgs args, String query) {
        return null;
    }

    public enum QueryArgs{
        GENRE,
        ARTIST,
        TITLE,
        ALBUM
    }
}
