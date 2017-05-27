package com.vpaliy.mediaplayer.service;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import java.util.List;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;


public class MusicPlaybackService extends MediaBrowserServiceCompat {

    private static final String LOG_TAG=MusicPlaybackService.class.getSimpleName();

    public static final String MEDIA_ID_ROOT="root";
    public static final String MEDIA_ID_EMPTY_ROOT="empty_root";

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;


    @Override
    public void onCreate() {
        super.onCreate();

        mediaSession = new MediaSessionCompat(getApplicationContext(), LOG_TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCallback());
        setSessionToken(mediaSession.getSessionToken());

    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        Log.d(LOG_TAG,"onGetRoot()");
        if(!clientPackageName.equals(getPackageName())){
            return new BrowserRoot(MEDIA_ID_ROOT,null);
        }
        return new BrowserRoot(MEDIA_ID_EMPTY_ROOT,null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {
        Log.d(LOG_TAG,"onLoadChildren()");
    }


    private final class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
            Log.d(LOG_TAG,"onPlay()");
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.d(LOG_TAG,"onPause");
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            Log.d(LOG_TAG,"onSeekTo");
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.d(LOG_TAG,"onStop");
        }

        @Override
        public void onPrepare() {
            super.onPrepare();
            Log.d(LOG_TAG,"onPrepare()");
        }
    }
}
