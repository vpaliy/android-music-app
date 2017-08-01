package com.vpaliy.mediaplayer.media.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
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
import com.vpaliy.mediaplayer.media.playback.MediaPlayback;
import com.vpaliy.mediaplayer.media.playback.MediaPlayback21;
import com.vpaliy.mediaplayer.media.playback.Playback;
import com.vpaliy.mediaplayer.media.playback.PlaybackManager;
import com.vpaliy.mediaplayer.media.playback.QueueManager;
import static com.vpaliy.mediaplayer.media.utils.MediaHelper.MEDIA_ID_EMPTY_ROOT;
import static com.vpaliy.mediaplayer.media.utils.MediaHelper.MEDIA_ID_ROOT;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MusicPlaybackService extends MediaBrowserServiceCompat
        implements PlaybackManager.PlaybackServiceCallback,
        PlaybackManager.MetadataUpdateListener{

    private static final String LOG_TAG=MusicPlaybackService.class.getSimpleName();

    public static final String ACTION_CMD="action:cmd";
    public static final String CMD_NAME="cmd:name";
    public static final String CMD_PAUSE="cmd:pause";

    private MediaSessionCompat mediaSession;
    private PlaybackManager playbackManager;

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
                playbackManager.updatePlaybackState(PlaybackStateCompat.STATE_NONE);
            }
        }.execute();
    }

    @Override
    public int onStartCommand(Intent startIntent, int flags, int startId) {
        if (startIntent != null) {
            String action = startIntent.getAction();
            String command = startIntent.getStringExtra(CMD_NAME);
            if (ACTION_CMD.equals(action)) {
                if (CMD_PAUSE.equals(command)) {
                    playbackManager.handlePauseRequest();
                }
            } else {
                // Try to handle the intent as a media button event wrapped by MediaButtonReceiver
                MediaButtonReceiver.handleIntent(mediaSession, startIntent);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onMetadataChanged(MediaMetadataCompat metadata) {
        mediaSession.setMetadata(metadata);
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
        mediaSession.setActive(true);
        Intent intent=new Intent(this,MusicPlaybackService.class);
        startService(intent);
    }

    @Override
    public void onPlaybackStop() {
        mediaSession.setActive(false);
        stopSelf();
    }

    @Override
    public void onPlaybackStateUpdated(PlaybackStateCompat stateCompat) {
        mediaSession.setPlaybackState(stateCompat);
    }

    @Override
    public void onNotificationRequired() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
}
