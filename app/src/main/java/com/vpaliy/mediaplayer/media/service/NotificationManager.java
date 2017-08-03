package com.vpaliy.mediaplayer.media.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.RemoteException;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.vpaliy.mediaplayer.MainActivity;
import com.vpaliy.mediaplayer.R;

public class NotificationManager extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 412;

    private static final int PLAYER_PENDING_INTENT_ID=3405;
    private static final int PAUSE_PENDING_INTENT_ID=3406;
    private static final int PLAY_PENDING_INTENT_ID=3407;
    private static final int PLAY_NEXT_PENDING_INTENT_ID=3408;
    private static final int PLAY_PREV_PENDING_INTENT_ID=3409;

    public static final String ACTION_PAUSE = "com.vpaliy.player.pause";
    public static final String ACTION_PLAY = "com.vpaliy.player.play";
    public static final String ACTION_PREV = "com.vpaliy.player.prev";
    public static final String ACTION_NEXT = "com.vpaliy.player.next";

    private MediaSessionCompat.Token token;
    private MediaControllerCompat controllerCompat;
    private MusicPlaybackService service;
    private MediaControllerCompat.TransportControls transportControls;

    private PlaybackStateCompat playbackState;
    private MediaMetadataCompat metadata;

    private NotificationManagerCompat notificationManager;
    private boolean isStarted;

    public NotificationManager(MusicPlaybackService service){
        this.service=service;
        updateSessionToken();
        notificationManager=NotificationManagerCompat.from(service);
    }

    private void updateSessionToken() {
        MediaSessionCompat.Token freshToken = service.getSessionToken();
        if (token == null && freshToken != null ||
                token!= null && !token.equals(freshToken)) {
            if (controllerCompat != null) {
                controllerCompat.unregisterCallback(callback);
            }
            token = freshToken;
            if (token != null) {
                try {
                    controllerCompat = new MediaControllerCompat(service, token);
                    transportControls=controllerCompat.getTransportControls();
                    controllerCompat.registerCallback(callback);
                }catch (RemoteException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    private MediaControllerCompat.Callback callback=new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            super.onPlaybackStateChanged(state);
            playbackState=state;
            if(state.getState()==PlaybackStateCompat.STATE_STOPPED
                    || state.getState()== PlaybackStateCompat.STATE_NONE){
                stopNotification();
            }else{
               updateNotification();
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat data) {
            super.onMetadataChanged(metadata);
            metadata=data;
            updateNotification();
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            updateSessionToken();
        }
    };

    private void stopNotification(){
        if(isStarted) {
            controllerCompat.unregisterCallback(callback);
            notificationManager.cancel(NOTIFICATION_ID);
            service.unregisterReceiver(this);
            service.stopForeground(true);
            isStarted = false;
        }
    }

    public void startNotification(){
        if(!isStarted) {
            metadata = controllerCompat.getMetadata();
            playbackState = controllerCompat.getPlaybackState();
            Notification notification = createNotification();
            if (notification != null) {
                controllerCompat.registerCallback(callback);
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_NEXT);
                filter.addAction(ACTION_PAUSE);
                filter.addAction(ACTION_PLAY);
                filter.addAction(ACTION_PREV);
                service.registerReceiver(this, filter);
                service.startForeground(NOTIFICATION_ID, notification);
                isStarted=true;
            }
        }
    }

    private void updateNotification(){
        if(!isStarted){
            startNotification();
        }else {
            Notification notification = createNotification();
            if (notification != null) {
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(NotificationManager.class.getSimpleName(),"onReceived()");
        switch (intent.getAction()) {
            case ACTION_PAUSE:
                transportControls.pause();
                break;
            case ACTION_PLAY:
                transportControls.play();
                break;
            case ACTION_NEXT:
                transportControls.skipToNext();
                break;
            case ACTION_PREV:
                transportControls.skipToPrevious();
                break;
        }
    }

    private PendingIntent contentIntent(Context context){
        Intent startActivityIntent=new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(context,
                PLAYER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context){
        return null;
    }

    private NotificationCompat.Action pause(Context context){
        Intent playPauseIntent=new Intent(context,MusicPlaybackService.class);
        playPauseIntent.setAction(ACTION_PAUSE);

        PendingIntent pausePendingIntent=PendingIntent.getBroadcast(context,
                PAUSE_PENDING_INTENT_ID,
                playPauseIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action(R.drawable.ic_pause_notif,"Pause",pausePendingIntent);
    }

    private NotificationCompat.Action next(Context context){
        Intent nextIntent=new Intent(context,MusicPlaybackService.class);
        nextIntent.setAction(ACTION_NEXT);

        PendingIntent nextPendingIntent=PendingIntent.getBroadcast(context,
                PLAY_NEXT_PENDING_INTENT_ID,
                nextIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action(R.drawable.ic_skip_next_notif,"Next",nextPendingIntent);
    }

    private NotificationCompat.Action prev(Context context){
        Intent prevIntent=new Intent(context,MusicPlaybackService.class);
        prevIntent.setAction(ACTION_PREV);

        PendingIntent prevPendingIntent=PendingIntent.getBroadcast(context,
                PLAY_PREV_PENDING_INTENT_ID,
                prevIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        return new NotificationCompat.Action(R.drawable.ic_skip_prev_notif,"Previous",prevPendingIntent);
    }

    private NotificationCompat.Action play(Context context){
        Intent prevIntent=new Intent(context,MusicPlaybackService.class);
        prevIntent.setAction(ACTION_PLAY);

        PendingIntent prevPendingIntent=PendingIntent.getService(context,
                PLAY_PENDING_INTENT_ID,
                prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return new NotificationCompat.Action(R.drawable.ic_play_notif,"Play",prevPendingIntent);
    }

    private Notification createNotification(){
        if(metadata==null||playbackState==null) return null;
        NotificationCompat.Builder builder=new NotificationCompat.Builder(service);
        builder.setStyle(new  NotificationCompat.MediaStyle()
            .setMediaSession(token)
            .setShowActionsInCompactView(1))
                .setColor(Color.WHITE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setUsesChronometer(true)
                .setSmallIcon(R.drawable.ic_play)
                .setContentIntent(contentIntent(service))
                .setContentTitle(metadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
                .setContentText(metadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE))
                .addAction(prev(service))
                .addAction(play(service))
                .addAction(next(service));
        return builder.build();
    }
}
