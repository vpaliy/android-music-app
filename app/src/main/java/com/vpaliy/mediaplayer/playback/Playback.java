package com.vpaliy.mediaplayer.playback;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.media.session.MediaSessionCompat.QueueItem;
import android.net.wifi.WifiManager;
import android.support.v4.media.session.PlaybackStateCompat;

public class Playback implements IPlayback,
        AudioManager.OnAudioFocusChangeListener{

    public static final float VOLUME_DUCK = 0.2f;
    public static final float VOLUME_NORMAL = 1.0f;

    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_FOCUSED  = 2;

    private int state;
    private int audioFocus=AUDIO_NO_FOCUS_NO_DUCK;
    private boolean onFocusGained;
    private volatile int currentPosition;
    private volatile String mediaId;
    private final WifiManager.WifiLock wifiLock;
    private Callback callback;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private Context context;

    public Playback(Context context){
        this.context=context;
        this.audioManager=AudioManager.class.cast(context.getSystemService(Context.AUDIO_SERVICE));
        this.wifiLock=WifiManager.class.cast(context.getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL,"uAmp_lock");
        this.state= PlaybackStateCompat.STATE_NONE;

    }

    @Override
    public void playNext() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void pause() {

    }

    @Override
    public void play(QueueItem item) {

    }

    @Override
    public void playPrevious() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getState() {
        return 0;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public void setState(int state) {
        this.state=state;
    }

    @Override
    public void seekTo(long position) {

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            audioFocus = AUDIO_FOCUSED;
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            boolean canDuck = focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK;
            audioFocus = canDuck ? AUDIO_NO_FOCUS_CAN_DUCK : AUDIO_NO_FOCUS_NO_DUCK;

            if (state == PlaybackStateCompat.STATE_PLAYING && !canDuck) {
                // If we don't have audio focus and can't duck, we save the information that
                // we were playing, so that we can resume playback once we get the focus back.
              //  mPlayOnFocusGain = true;
            }
        }
    }

    @Override
    public void stop() {
        state=PlaybackStateCompat.STATE_STOPPED;
        if(callback!=null) callback.onStateChanged(state);
        currentPosition=getStreamPosition();
    }

    private int getStreamPosition(){
        return mediaPlayer!=null?mediaPlayer.getCurrentPosition():currentPosition;
    }

    @Override
    public void start() {

    }

    @Override
    public String getId() {
        return null;
    }
}
