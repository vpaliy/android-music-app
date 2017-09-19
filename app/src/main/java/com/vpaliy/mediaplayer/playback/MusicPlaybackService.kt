package com.vpaliy.mediaplayer.playback

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaButtonReceiver
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.vpaliy.mediaplayer.FitnessSound
import com.vpaliy.mediaplayer.ui.player.PlayerActivity
import javax.inject.Inject

class MusicPlaybackService : MediaBrowserServiceCompat(),
        PlaybackManager.PlaybackServiceCallback, PlaybackManager.MetadataUpdateListener {

    private lateinit var mediaSession:MediaSessionCompat
    private lateinit var notification:TrackNotification
    @Inject lateinit var playbackManager: PlaybackManager

    init {
        FitnessSound.app().playbackComponent().inject(this)
    }

    override fun onCreate() {
        super.onCreate()
        mediaSession=MediaSessionCompat(applicationContext, TAG)
        notification=TrackNotification(this)
        playbackManager.serviceCallback = this
        playbackManager.updateListener = this
        mediaSession.setCallback(playbackManager.mediaSessionCallback)
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS)
        sessionToken = mediaSession.sessionToken
        val context = applicationContext
        val intent = Intent(context, PlayerActivity::class.java)
        val pi = PendingIntent.getActivity(context, 99,
                intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mediaSession.setSessionActivity(pi)
        playbackManager.updatePlaybackState(PlaybackStateCompat.STATE_NONE)
    }

    override fun onStartCommand(startIntent: Intent?, flags: Int, startId: Int): Int {
        if (startIntent != null) {
            val action = startIntent.action
            action?.let {
                when(action){
                    MediaTasks.ACTION_NEXT->playbackManager.handleNextRequest()
                    MediaTasks.ACTION_PREV->playbackManager.handlePrevRequest()
                    MediaTasks.ACTION_PAUSE->playbackManager.handlePauseRequest()
                    MediaTasks.ACTION_PLAY->playbackManager.handleResumeRequest()
                    MediaTasks.ACTION_STOP->stopSelf()
                    else ->  MediaButtonReceiver.handleIntent(mediaSession, startIntent)
                }
            }
        }
        return Service.START_NOT_STICKY
    }

    override fun onMetadataChanged(metadata: MediaMetadataCompat) {
        mediaSession.setMetadata(metadata)
        notification.updateMetadata(metadata)
    }

    override fun onMetadataRetrieveError() {
    }

    override fun onPlaybackStart() {
        mediaSession.isActive = true
        val intent = Intent(this, MusicPlaybackService::class.java)
        startService(intent)
    }

    override fun onPlaybackStop() {
        mediaSession.isActive = false
        notification.pauseNotification()
    }

    override fun onPlaybackStateUpdated(newState: PlaybackStateCompat) {
        Log.d("MusicPlaybackService","onPlaybackState"+newState.state)
        mediaSession.setPlaybackState(newState)
        notification.updatePlaybackState(newState)
    }

    override fun onNotificationRequired()=notification.startNotification()

    override fun onDestroy() {
        mediaSession.release()
        stopForeground(true)
        super.onDestroy()
    }

    override fun onGetRoot(clientPackageName: String, clientUid: Int, rootHints: Bundle?): MediaBrowserServiceCompat.BrowserRoot? {
        return if (clientPackageName != packageName) {
             MediaBrowserServiceCompat.BrowserRoot("root", null)
        }else MediaBrowserServiceCompat.BrowserRoot("empty", null)
    }

    override fun onLoadChildren(parentId: String, result: MediaBrowserServiceCompat.Result<List<MediaBrowserCompat.MediaItem>>) {}

    companion object {
        private val TAG = MusicPlaybackService::class.java.simpleName
    }
}