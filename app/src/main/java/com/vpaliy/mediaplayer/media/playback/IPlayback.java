package com.vpaliy.mediaplayer.media.playback;


import android.support.v4.media.session.MediaSessionCompat.QueueItem;

public interface IPlayback {
    void start();
    void stop();
    void pause();
    void setState(int state);
    void setCurrentStreamPosition(long position);
    void setCallback(Callback callback);
    void play(QueueItem item);
    void seekTo(long position);
    void setCurrentMediaId(String mediaId);
    boolean isPlaying();
    boolean isConnected();
    String getCurrentMediaId();
    int getState();
    long getCurrentStreamPosition();

    /* Notifies the service about events which happen with the player */
    interface Callback{
        void onFinished();
        void onStateChanged(int state);
        void onError(Throwable ex);
        void onMediaSet(String mediaId);
    }
}
