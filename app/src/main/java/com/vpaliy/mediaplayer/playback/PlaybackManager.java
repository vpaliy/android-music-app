package com.vpaliy.mediaplayer.playback;


import android.support.v4.media.session.MediaSessionCompat;

public class PlaybackManager {

    private final IPlayback playback;
    private final MediaSessionCallback mediaSessionCallback;

    public PlaybackManager(IPlayback iPlayback){
        this.playback=iPlayback;
        this.mediaSessionCallback=new MediaSessionCallback();
    }


    private final class MediaSessionCallback extends MediaSessionCompat.Callback {

        @Override
        public void onPlay() {
            super.onPlay();
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
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }
    }
}
