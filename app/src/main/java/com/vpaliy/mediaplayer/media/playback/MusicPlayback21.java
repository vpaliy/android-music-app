package com.vpaliy.mediaplayer.media.playback;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.vpaliy.mediaplayer.media.service.MusicPlaybackService;

public class MusicPlayback21 implements IPlayback,
        AudioManager.OnAudioFocusChangeListener,
        ExoPlayer.EventListener{

    private final static String TAG=MusicPlayback21.class.getSimpleName();

    private SimpleExoPlayer exoPlayer;
    public static final float VOLUME_DUCK = 0.2f;
    public static final float VOLUME_NORMAL = 1.0f;

    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_FOCUSED  = 2;

    private int state;
    private int audioFocusState=AUDIO_NO_FOCUS_NO_DUCK;
    private boolean noisyReceiverRegistered;
    //private final WifiManager.WifiLock wifiLock;
    private Callback callback;
    private AudioManager audioManager;
    private Context context;
    private volatile long currentPosition;
    private volatile String currentMediaId;
    private WifiManager.WifiLock wifiLock;

    private final IntentFilter audioBecomingNoisyIntent=
            new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);

    private final BroadcastReceiver audioNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
                if (isPlaying()) {
                    Log.d(TAG,"Headphones disconnected");
                    Intent i=new Intent(context, MusicPlaybackService.class);
                    i.setAction(MusicPlaybackService.ACTION_CMD);
                    i.putExtra(MusicPlaybackService.CMD_NAME,MusicPlaybackService.CMD_PAUSE);
                    context.startService(i);
                }
            }
        }
    };

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPositionDiscontinuity() {
        // Nothing to do.
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        // Nothing to do.
    }

    private boolean requestFocus(){
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            audioFocusState = AUDIO_FOCUSED;
        } else {
            audioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
        }
        return audioFocusState==AUDIO_FOCUSED;
    }

    private void releaseAudioFocus(){

    }

    @Override
    public void play(MediaSessionCompat.QueueItem item) {
        if(requestFocus()) {
            registerAudioNoisyReceiver();
            acquireWifiLock();
            configurePlayer(item.getDescription().getMediaId());
        }
    }

    private void configurePlayer(String source){
        if(exoPlayer==null){
            exoPlayer= ExoPlayerFactory.newSimpleInstance(context,new DefaultTrackSelector());
            exoPlayer.addListener(this);
        }
        exoPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(
                        context, Util.getUserAgent(context, "uamp"), null);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        MediaSource mediaSource =
                new ExtractorMediaSource(
                        Uri.parse(source), dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.prepare(mediaSource);

        if(audioFocusState==AUDIO_NO_FOCUS_NO_DUCK){
            pause();
        }else{
            if(audioFocusState==AUDIO_NO_FOCUS_CAN_DUCK){
                exoPlayer.setVolume(VOLUME_DUCK);
            }else{
                exoPlayer.setVolume(VOLUME_NORMAL);
            }

            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void acquireWifiLock(){
        wifiLock.acquire();
    }

    private void releaseResources(boolean releasePlayer) {
        Log.d(TAG, "releaseResources. releasePlayer="+releasePlayer);

        // Stops and releases player (if requested and available).
        if (releasePlayer && exoPlayer != null) {
            exoPlayer.release();
            exoPlayer.removeListener(this);
            exoPlayer = null;
        }

        if (wifiLock.isHeld()) {
            wifiLock.release();
        }
    }

    @Override
    public void pause() {

    }

    private void registerAudioNoisyReceiver(){

    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_GAIN:
                audioFocusState=AUDIO_FOCUSED;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                audioFocusState=AUDIO_NO_FOCUS_CAN_DUCK;
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS:
                audioFocusState=AUDIO_NO_FOCUS_NO_DUCK;
                break;
        }
    }

    @Override
    public String getCurrentMediaId() {
        return currentMediaId;
    }


    @Override
    public int getState() {
        return 0;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback=callback;
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
