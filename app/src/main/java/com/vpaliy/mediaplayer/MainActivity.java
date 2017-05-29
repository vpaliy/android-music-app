package com.vpaliy.mediaplayer;

import android.content.ComponentName;
import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.vpaliy.mediaplayer.service.MusicPlaybackService;

public class MainActivity extends AppCompatActivity{

    private static final String TAG=MainActivity.class.getSimpleName();

    private MediaBrowserCompat browserCompat;
    private MediaBrowserCompat.ConnectionCallback connectionCallback=new MediaBrowserCompat.ConnectionCallback(){


        @Override
        public void onConnected()  {
            super.onConnected();
            Log.d(TAG,"onConnected()");
            MediaSessionCompat.Token token=browserCompat.getSessionToken();
            try {
                MediaControllerCompat mediaController =new MediaControllerCompat(MainActivity.this,
                                token);
                // Save the controller
                MediaControllerCompat.setMediaController(MainActivity.this, mediaController);
                buildTransportUI();
            }catch (RemoteException ex){
                ex.printStackTrace();
            }


        }

        @Override
        public void onConnectionFailed() {
            super.onConnectionFailed();
            Log.d(TAG,"onConnectionFailed()");
        }

        @Override
        public void onConnectionSuspended() {
            super.onConnectionSuspended();
            Log.d(TAG,"onConnectionSuspended()");
        }
    };

    private MediaControllerCompat.Callback controllerCallback=new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            Log.d(TAG,"PlaybackStateChanged()");
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            super.onMetadataChanged(metadata);
            Log.d(TAG,"onMetadataChanged()");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browserCompat=new MediaBrowserCompat(this,
                new ComponentName(this, MusicPlaybackService.class),
                connectionCallback,null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        browserCompat.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(MediaControllerCompat.getMediaController(this)!=null){
            MediaControllerCompat.getMediaController(this).unregisterCallback(controllerCallback);
        }
        browserCompat.disconnect();
    }

    private void buildTransportUI(){
        Button playPause=(Button)(findViewById(R.id.play_pause));
        final MediaControllerCompat controllerCompat=MediaControllerCompat.getMediaController(MainActivity.this);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int state=controllerCompat.getPlaybackState().getState();
                if(state== PlaybackStateCompat.STATE_PLAYING){
                    controllerCompat.getTransportControls().pause();
                }else{
                    controllerCompat.getTransportControls().play();
                }
            }
        });
        MediaMetadataCompat metadata = controllerCompat.getMetadata();
        PlaybackStateCompat pbState = controllerCompat.getPlaybackState();

        // Register a Callback to stay in sync
        controllerCompat.registerCallback(controllerCallback);

    }
}
