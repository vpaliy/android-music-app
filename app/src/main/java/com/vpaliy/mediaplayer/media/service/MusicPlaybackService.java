package com.vpaliy.mediaplayer.media.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import com.vpaliy.mediaplayer.MainActivity;
import com.vpaliy.mediaplayer.media.model.RemoteJSONSource;
import com.vpaliy.mediaplayer.media.playback.MediaPlayback21;
import com.vpaliy.mediaplayer.media.playback.Playback;
import com.vpaliy.mediaplayer.media.playback.PlaybackManager;
import com.vpaliy.mediaplayer.media.playback.QueueManager;

import static com.vpaliy.mediaplayer.media.service.MediaTasks.ACTION_PAUSE;
import static com.vpaliy.mediaplayer.media.service.MediaTasks.ACTION_PLAY;
import static com.vpaliy.mediaplayer.media.service.MediaTasks.ACTION_PREV;
import static com.vpaliy.mediaplayer.media.utils.MediaHelper.MEDIA_ID_EMPTY_ROOT;
import static com.vpaliy.mediaplayer.media.utils.MediaHelper.MEDIA_ID_ROOT;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MusicPlaybackService extends MediaBrowserServiceCompat
        implements PlaybackManager.PlaybackServiceCallback,
        PlaybackManager.MetadataUpdateListener{

    private static final String LOG_TAG=MusicPlaybackService.class.getSimpleName();

    private static final long STOP_DELAY=10000; //30 sec before the service stops

    private MediaSessionCompat mediaSession;
    private PlaybackManager playbackManager;
    private MusicNotification musicNotification;
    private DelayedStopHandler stopHandler=new DelayedStopHandler(this);


    @Override
    public void onCreate() {
        super.onCreate();
        new AsyncTask<Void,Void,Iterator<MediaMetadataCompat>>(){
            @Override
            protected Iterator<MediaMetadataCompat> doInBackground(Void... params) {
                return new RemoteJSONSource().iterator();
            }

            @Override
            protected void onPostExecute(Iterator<MediaMetadataCompat> iterator) {
                playbackManager=new PlaybackManager(playback(),queueManager(iterator));
                playbackManager.setServiceCallback(MusicPlaybackService.this);
                playbackManager.setUpdateListener(MusicPlaybackService.this);
                mediaSession=new MediaSessionCompat(getApplicationContext(),LOG_TAG);
                mediaSession.setCallback(playbackManager.getMediaSessionCallback());
                mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
                setSessionToken(mediaSession.getSessionToken());
                Context context = getApplicationContext();
                Intent intent = new Intent(context, MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(context, 99,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mediaSession.setSessionActivity(pi);
                musicNotification=new MusicNotification(MusicPlaybackService.this);
                playbackManager.updatePlaybackState(PlaybackStateCompat.STATE_NONE);
            }
        }.execute();
    }

    @Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
        Log.d(LOG_TAG,"onStartCommand()");
        if (startIntent != null) {
            String action = startIntent.getAction();
            if(action!=null) {
                if (action.equals(ACTION_PAUSE)) {
                    playbackManager.handlePauseRequest();
                } else if (action.equals(ACTION_PLAY)) {
                    playbackManager.handleResumeRequest();
                } else if (action.equals(ACTION_PREV)) {

                } else {
                    // Try to handle the intent as a media button event wrapped by MediaButtonReceiver
                    MediaButtonReceiver.handleIntent(mediaSession, startIntent);
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        Log.d(LOG_TAG,"onMetadataChanged");
        mediaSession.setMetadata(metadata);
        musicNotification.updateMetadata(metadata);
    }

    @Override
    public void onCurrentQueueIndexUpdated(int queueIndex) {

    }

    @Override
    public void onMetadataRetrieveError() {

    }

    private Playback playback(){
        AudioManager audioManager=AudioManager.class.cast(getApplicationContext().getSystemService(Context.AUDIO_SERVICE));
        WifiManager.WifiLock wifiManager=((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "uAmp_lock");
        return new MediaPlayback21(getApplicationContext(),audioManager,wifiManager);
    }

    private QueueManager queueManager(Iterator<MediaMetadataCompat> iterator){
        QueueManager queueManager=new QueueManager();
        List<MediaMetadataCompat> list=new LinkedList<>();
        while(iterator.hasNext()) {
            MediaMetadataCompat metadataCompat=iterator.next();
            Log.d(MusicPlaybackService.class.getSimpleName(),metadataCompat.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI));
            list.add(iterator.next());
        }
        queueManager.setQueueData(list);
        return queueManager;
    }
    @Override
    public void onPlaybackStart() {
        Log.d(LOG_TAG,"onPlaybackStart");
        mediaSession.setActive(true);
        stopHandler.removeCallbacksAndMessages(null);
        Intent intent=new Intent(this,MusicPlaybackService.class);
        startService(intent);
    }

    @Override
    public void onPlaybackPause() {
        Log.d(LOG_TAG,"onPlaybackPause");
        mediaSession.setActive(false);
    }

    @Override
    public void onPlaybackStop() {
        Log.d(LOG_TAG,"onPlaybackStop");
        mediaSession.setActive(false);
        stopHandler.removeCallbacksAndMessages(null);
        stopHandler.sendEmptyMessageDelayed(0,STOP_DELAY);
        stopForeground(true);
    }

    @Override
    public void onPlaybackStateUpdated(PlaybackStateCompat stateCompat) {
        Log.d(LOG_TAG,"onPlaybackStateUpdated");
        mediaSession.setPlaybackState(stateCompat);
        musicNotification.updatePlaybackState(stateCompat);
    }

    @Override
    public void onNotificationRequired() {
        Log.d(LOG_TAG,"onNotificationRequired");
        musicNotification.startNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG,"onDestroy()");
        playbackManager.handleStopRequest();
        musicNotification.stopNotification();
        stopHandler.removeCallbacksAndMessages(null);
        mediaSession.release();
    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        Log.d(LOG_TAG,"onGetRoot()");
        if(!clientPackageName.equals(getPackageName())){
            return new BrowserRoot(MEDIA_ID_ROOT,null);
        }
        return new BrowserRoot(MEDIA_ID_EMPTY_ROOT,null);
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    private static class DelayedStopHandler extends Handler {
        private final WeakReference<MusicPlaybackService> mWeakReference;

        private DelayedStopHandler(MusicPlaybackService service) {
            mWeakReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.d(LOG_TAG,"Handling message before destroying");
            MusicPlaybackService service = mWeakReference.get();
            Log.d(this.getClass().getSimpleName(),"Null:"+(service==null));
            if(service!=null){
                boolean stopThis=service.playbackManager.getPlayback()==null
                        ||!service.playbackManager.getPlayback().isPlaying();
                if(stopThis){
                    service.musicNotification.stopNotification();
                    service.stopSelf();
                }
            }
        }
    }
}
