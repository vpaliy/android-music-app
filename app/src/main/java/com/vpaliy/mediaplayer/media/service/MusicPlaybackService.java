package com.vpaliy.mediaplayer.media.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;

import java.util.ArrayList;
import java.util.List;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.vpaliy.mediaplayer.media.model.MediaProvider;
import com.vpaliy.mediaplayer.media.model.Query;
import com.vpaliy.mediaplayer.media.playback.Playback;
import com.vpaliy.mediaplayer.media.playback.PlaybackManager;
import com.vpaliy.mediaplayer.media.playback.QueueManager;
import com.vpaliy.mediaplayer.media.utils.MediaHelper;

import static com.vpaliy.mediaplayer.media.utils.MediaHelper.MEDIA_ID_EMPTY_ROOT;
import static com.vpaliy.mediaplayer.media.utils.MediaHelper.MEDIA_ID_ROOT;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MusicPlaybackService extends MediaBrowserServiceCompat
        implements Playback.Callback{

    private static final String LOG_TAG=MusicPlaybackService.class.getSimpleName();

    public static final String ACTION_CMD="action:cmd";
    public static final String CMD_NAME="cmd:name";
    public static final String CMD_PAUSE="cmd:pause";

    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    private MediaProvider<Query> dataProvider;

    public MusicPlaybackService(){}

    @Inject
    public MusicPlaybackService(@NonNull MediaSessionCompat mediaSession,
                                @NonNull MediaProvider<Query> dataProvider){
        this.mediaSession=mediaSession;
        this.dataProvider=dataProvider;
        stateBuilder=new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE);
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onPlay() {
        mediaSession.setActive(true);
        Intent intent=new Intent(this,MusicPlaybackService.class);
        startService(intent);
    }

    @Override
    public void onStop() {
        mediaSession.setActive(false);
        stopSelf();
    }

    @Override
    public void onCompletetion() {

    }

    @Override
    public void onError() {

    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback{

        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onPause() {
            super.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaSession.release();
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
        if(MEDIA_ID_EMPTY_ROOT.equals(parentId)){
            result.sendResult(new ArrayList<>());
        }else if(dataProvider.isInitialized()){
            result.sendResult(MediaHelper.getChildren(parentId,getResources()));
        }else{
            result.detach();
        }
    }
}
