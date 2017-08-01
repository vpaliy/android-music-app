package com.vpaliy.mediaplayer.media.playback;

import android.media.session.MediaSession;
import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;

import javax.inject.Inject;

public class PlaybackManager implements Playback.Callback {

    private Playback playback;

    @Inject
    public PlaybackManager(Playback playback){
        this.playback=playback;
    }

    public void handlePlayRequest(String url){
        playback.play(url);
    }

    public void handlePauseRequest(){
        playback.pause();
    }

    public void handleStopRequest(){
        playback.stop();
    }

    @Override
    public void onPlay() {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onCompletetion() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onPause() {

    }


    private class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();

        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
            handlePlayRequest(mediaId);
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }
    }
}
