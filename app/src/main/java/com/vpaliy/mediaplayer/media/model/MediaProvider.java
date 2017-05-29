package com.vpaliy.mediaplayer.media.model;

import android.support.v4.media.MediaMetadataCompat;

public interface MediaProvider<Q> {
    MediaMetadataCompat at(int index);
    MediaMetadataCompat byId(String mediaId);
    MediaMetadataCompat search(Q queryParameter, String query);
    int count();
}
