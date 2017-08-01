package com.vpaliy.mediaplayer.media.playback;


import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.annotation.NonNull;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * PlaybackManager — is the  class which pulls the strings.
 * It controls the playback, manages the music  queue, and notifies everybody around about changes.
 * Basically, it’s an additional layer that wraps up a playback. Just another place to keep all related abstractions together.
 * It has a reference to the MediaSessionCallback.class which serves as a listener for any events from the UI (play/pause, seek to, rewind, next/previous).
 * When a new event arrives to the listener, it notifies the PlaybackManager by calling an appropriate method.
 * For example, when the listener gets the Play event, it calls the handlePlayRequest method from the PlaybackManager.
 */

@Singleton
public class PlaybackManager implements Playback.Callback {

    private final Playback playback;
    private final MediaSessionCallback mediaSessionCallback;
    private final PlaybackManagerCallback managerCallback;
    private final QueueManager queueManager;
    private final PlaybackStateCompat.Builder stateBuilder;

    @Inject
    public PlaybackManager(@NonNull Playback iPlayback,
                           @NonNull QueueManager queueManager,
                           @NonNull PlaybackManagerCallback managerCallback){
        this.playback=iPlayback;
        this.mediaSessionCallback=new MediaSessionCallback();
        this.queueManager=queueManager;
        this.stateBuilder=new PlaybackStateCompat.Builder();
        this.managerCallback=managerCallback;
        this.playback.setCallback(this);
    }

    public Playback getPlayback() {
        return playback;
    }

    public MediaSessionCallback getMediaSessionCallback() {
        return mediaSessionCallback;
    }

    public void handlePlayRequest(){
        MediaSessionCompat.QueueItem queueItem=queueManager.getCurrent();
        if(queueItem!=null){
            managerCallback.onPlaybackStart();
          //  playback.play(queueItem);
        }
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onStop() {

    }

    public void handlePauseRequest(){
        if(playback.isPlaying()){
            playback.pause();
            //has to stop the service
            managerCallback.onPlaybackStop();
        }
    }

    public void handleStopRequest(Throwable error){
        playback.stop();
        managerCallback.onPlaybackStop();
        managerCallback.onPlaybackError();
    }

    private long getAvailableActions() {
        long actions =  PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT;
        if (playback.isPlaying()) {
            actions |= PlaybackStateCompat.ACTION_PAUSE;
        } else {
            actions |= PlaybackStateCompat.ACTION_PLAY;
        }
        return actions;
    }


    @Override
    public void onError() {
        managerCallback.onPlaybackError();
    }

    @Override
    public void onCompletetion() {

    }

    private final class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            handlePlayRequest();
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
            queueManager.setCurrentItem(mediaId);
        }

        @Override
        public void onPause() {
            super.onPause();
            handlePauseRequest();
        }

        @Override
        public void onStop() {
            super.onStop();
            handleStopRequest(null);
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            if(queueManager.skipQueuePosition(1)){
                handlePlayRequest();
            }else{
                handleStopRequest(null);
            }
            queueManager.updateMetadata();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
            playback.seekTo((int)pos);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            if(queueManager.skipQueuePosition(-1)){
                handlePlayRequest();
            }else{
                handleStopRequest(null);
            }
            queueManager.updateMetadata();
        }
    }

    public interface PlaybackManagerCallback{
        void onPlaybackStart();
        void onPlaybackPause();
        void onNotificationRequired();
        void onPlaybackStop();
        void onPlaybackError();
    }

}
