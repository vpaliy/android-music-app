package com.vpaliy.mediaplayer.media.playback;

public class MediaPlayback21 implements Playback {

    private Callback callback;


    @Override
    public void play(String mediaUrl) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void seekTo(int position) {

    }

    @Override
    public long getPosition() {
        return 0;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }
}