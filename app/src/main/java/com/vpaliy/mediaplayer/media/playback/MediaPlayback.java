package com.vpaliy.mediaplayer.media.playback;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.PowerManager;

import java.io.IOException;

public class MediaPlayback extends BasePlayback
    implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener{

    private MediaPlayer player;

    public MediaPlayback(Context context,
                         AudioManager audioManager,
                         WifiManager.WifiLock wifiLock){
        super(context,audioManager,wifiLock);
    }

    @Override
    public void startPlayer() {
        if(player==null){
            player=new MediaPlayer();
            player.setOnErrorListener(this);
            player.setWakeMode(context, PowerManager.PARTIAL_WAKE_LOCK);
        }
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(currentUrl);
            player.prepareAsync();
            player.setOnPreparedListener(this);
            player.start();
        }catch (IOException ex){
            ex.printStackTrace();
            //notify UI
            if(callback!=null) callback.onError();
        }
    }

    @Override
    public void updatePlayer() {
        if(player!=null){
            switch (focusState){
                case AUDIO_FOCUSED:
                    player.setVolume(0f,VOLUME_NORMAL);
                    break;
                case AUDIO_NO_FOCUS_CAN_DUCK:
                    player.setVolume(0f,VOLUME_DUCK);
                    break;
                case AUDIO_NO_FOCUS_NO_DUCK:
                    pausePlayer();
            }
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        switch (focusState){
            case AUDIO_FOCUSED:
                player.setVolume(0f,VOLUME_NORMAL);
                break;
            case AUDIO_NO_FOCUS_CAN_DUCK:
                player.setVolume(0f,VOLUME_DUCK);
                break;
            case AUDIO_NO_FOCUS_NO_DUCK:
                //TODO probably need to pause
                return;
        }
        player.start();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        if(callback!=null) callback.onError();
        stopPlayer();
        return true;
    }

    @Override
    public void pausePlayer() {
        if(isPlaying()){
            player.pause();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(callback!=null) callback.onCompletetion();
    }

    @Override
    public void resumePlayer() {
        if(player!=null){
            if(!player.isPlaying()){
                player.start();
            }
        }
    }

    @Override
    public void stopPlayer() {
        if (player != null) {
            player.release();
            player=null;
        }
    }

    @Override
    public void seekTo(int position) {
        if(player!=null){
            player.seekTo(position);
        }
    }

    @Override
    public long getPosition() {
        return player!=null?player.getCurrentPosition():0;
    }

    @Override
    public boolean isPlaying() {
        if(player!=null){
            return player.isPlaying();
        }
        return false;
    }
}
