package com.vpaliy.mediaplayer.playback;


import android.support.v4.media.session.MediaSessionCompat.QueueItem;

public interface IPlayback {
    void start();
    void stop();
    void pause();
    void playNext();
    void playPrevious();
    void setState(int state);
    void play(QueueItem item);
    void seekTo(long position);
    void setId(String id);
    boolean isPlaying();
    boolean isConnected();
    String getId();
    int getState();

    interface Callback{
        void onFinished();
        void onStateChanged(int state);
        void onError(Throwable ex);
        void onMediaSet(String mediaId);
    }
}
