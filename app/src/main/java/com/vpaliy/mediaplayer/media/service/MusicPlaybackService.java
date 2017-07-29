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
        implements PlaybackManager.PlaybackManagerCallback,
        QueueManager.MetadataUpdateListener{

    private static final String LOG_TAG=MusicPlaybackService.class.getSimpleName();

    private final  MediaSessionCompat mediaSession;
    private final PlaybackStateCompat.Builder stateBuilder;
    private final PlaybackManager playbackManager;
    private final MediaProvider<Query> dataProvider;

    @Inject
    public MusicPlaybackService(@NonNull MediaSessionCompat mediaSession,
                                @NonNull PlaybackManager manager,
                                @NonNull MediaProvider<Query> dataProvider){
        this.mediaSession=mediaSession;
        this.playbackManager=manager;
        this.dataProvider=dataProvider;
        stateBuilder=new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY | PlaybackStateCompat.ACTION_PLAY_PAUSE);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(playbackManager.getMediaSessionCallback());
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        setSessionToken(mediaSession.getSessionToken());

        playbackManager.updatePlaybackState(null);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        playbackManager.handleStopRequest(null);
        mediaSession.release();
    }

    @Override
    public void onPlaybackStart() {
        mediaSession.setActive(true);
        Intent intent=new Intent(this,MusicPlaybackService.class);
        startService(intent);
    }

    @Override
    public void onPlaybackStateUpdated(PlaybackStateCompat newState) {
        mediaSession.setPlaybackState(newState);
    }

    @Override
    public void onNotificationRequired() {

    }

    @Override
    public void onPlaybackStop() {
        mediaSession.setActive(false);
        stopSelf();
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        mediaSession.setMetadata(metadata);
    }

    @Override
    public void onCurrentQueueIndexUpdated(int queueIndex) {
        playbackManager.handlePlayRequest();
    }

    @Override
    public void onMetadataRetrieveError() {
        playbackManager.updatePlaybackState(new Exception("No data exception"));
    }

    @Override
    public void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue) {
        mediaSession.setQueue(newQueue);
        mediaSession.setQueueTitle(title);
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
