package com.vpaliy.mediaplayer.media.model;

import android.support.v4.media.MediaMetadataCompat;

public  class MusicProvider implements MediaProvider<Query> {

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
    public MediaMetadataCompat search(Query args, String query) {
        return null;
    }

    @Override
    public boolean isInitialized() {
        return false;
    }

}
