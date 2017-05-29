package com.vpaliy.mediaplayer.media.playback;


import android.support.v4.media.session.MediaSessionCompat;

public class MusicPlayback21 implements IPlayback {

    @Override
    public void play(MediaSessionCompat.QueueItem item) {

    }

    @Override
    public void pause() {

    }


    @Override
    public String getCurrentMediaId() {
        return null;
    }

    @Override
    public void setCurrentStreamPosition(long position) {

    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public void setCallback(Callback callback) {

    }

    @Override
    public void setCurrentMediaId(String mediaId) {

    }

    @Override
    public void setState(int state) {

    }

    @Override
    public void seekTo(long position) {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public long getCurrentStreamPosition() {
        return 0;
    }
}
