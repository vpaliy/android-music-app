package com.vpaliy.mediaplayer.playback;


import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

public class PlaybackManager implements IPlayback.Callback {

    private final IPlayback playback;
    private final MediaSessionCallback mediaSessionCallback;
    private final PlaybackManagerCallback managerCallback;
    private final QueueManager queueManager;
    private final PlaybackStateCompat.Builder stateBuilder;

    public PlaybackManager(@NonNull IPlayback iPlayback,
                           @NonNull QueueManager queueManager,
                           @NonNull PlaybackManagerCallback managerCallback){
        this.playback=iPlayback;
        this.mediaSessionCallback=new MediaSessionCallback();
        this.queueManager=queueManager;
        this.stateBuilder=new PlaybackStateCompat.Builder();
        this.managerCallback=managerCallback;
    }

    public IPlayback getPlayback() {
        return playback;
    }

    public MediaSessionCallback getMediaSessionCallback() {
        return mediaSessionCallback;
    }

    public void handlePlayRequest(){
        MediaSessionCompat.QueueItem queueItem=queueManager.getCurrent();
        if(queueItem!=null){
            managerCallback.onPlaybackStart();
            playback.play(queueItem);
        }
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
        updatePlaybackState(error);
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

    public void updatePlaybackState(Throwable error){
        if(error!=null) error.printStackTrace();
        long position=PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN;
        if(playback.isConnected()){
            position=playback.getCurrentStreamPosition();
        }
        stateBuilder.setActions(getAvailableActions());
        final String message=error!=null?error.getMessage():null;
        stateBuilder.setErrorMessage(message);
        final int state=error!=null?PlaybackStateCompat.STATE_ERROR:playback.getState();
        stateBuilder.setState(state,position,1.0f, SystemClock.elapsedRealtime());
        MediaSessionCompat.QueueItem queueItem=queueManager.getCurrent();
        if (queueItem != null) {
            stateBuilder.setActiveQueueItemId(queueItem.getQueueId());
        }
        managerCallback.onPlaybackStateUpdated(stateBuilder.build());
        if (state == PlaybackStateCompat.STATE_PLAYING || state == PlaybackStateCompat.STATE_PAUSED) {
            managerCallback.onNotificationRequired();
        }
    }

    @Override
    public void onError(Throwable ex) {
        updatePlaybackState(ex);
    }

    @Override
    public void onFinished() {
        if(queueManager.skipQueuePosition(1)){
            handlePlayRequest();
            queueManager.updateMetadata();
        }else{
            handleStopRequest(null);
        }
    }

    @Override
    public void onMediaSet(String mediaId) {
        queueManager.setCurrentItem(mediaId);
    }

    @Override
    public void onStateChanged(int state) {
        updatePlaybackState(null);
    }

    private final class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            handlePlayRequest();
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
        void onNotificationRequired();
        void onPlaybackStop();
        void onPlaybackStateUpdated(PlaybackStateCompat newState);
    }

}
