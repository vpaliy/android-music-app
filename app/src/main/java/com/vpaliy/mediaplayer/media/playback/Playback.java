package com.vpaliy.mediaplayer.media.playback;

/* The base interface for any playback implementation */
public interface Playback  {

    void play(String mediaUrl);
    void pause();
    void stop();
    void seekTo(int position);
    void setCallback(Callback callback);
    boolean isPlaying();
    long getPosition();

    /* This interface is used to notify others about changes in the player*/
    interface Callback {
        void onCompletetion();
        void onPause();
        void onPlay();
        void onStop();
        void onError();
    }
}
