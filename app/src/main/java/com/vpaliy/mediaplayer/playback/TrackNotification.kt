package com.vpaliy.mediaplayer.playback

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.media.MediaBrowserServiceCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.support.v7.app.NotificationCompat
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.player.PlayerActivity
import com.vpaliy.mediaplayer.playback.MediaTasks.ACTION_PAUSE
import com.vpaliy.mediaplayer.playback.MediaTasks.ACTION_PLAY
import com.vpaliy.mediaplayer.playback.MediaTasks.ACTION_PREV
import com.vpaliy.mediaplayer.playback.MediaTasks.ACTION_NEXT
import com.vpaliy.mediaplayer.playback.MediaTasks.ACTION_STOP
import android.support.v4.app.NotificationCompat.Action

class TrackNotification(private val service: MediaBrowserServiceCompat) {

    private var playbackState: PlaybackStateCompat? = null
    private var mediaMetadata: MediaMetadataCompat? = null
    private var isStarted: Boolean = false
    private var token: MediaSessionCompat.Token?=null
    private val manager: NotificationManagerCompat = NotificationManagerCompat.from(service)

    init {
        this.token = service.sessionToken
        manager.cancel(NOTIFICATION_ID)
    }

    fun updatePlaybackState(playbackState: PlaybackStateCompat) {
        this.playbackState = playbackState
        if (playbackState.state == PlaybackStateCompat.STATE_STOPPED || playbackState.state == PlaybackStateCompat.STATE_NONE) {
            stopNotification()
        } else {
            updateNotification()
        }
    }

    fun startNotification() {
        if (!isStarted) {
            val notification = createNotification()
            if (notification != null) {
                service.startForeground(NOTIFICATION_ID, notification)
                isStarted = true
            }
        }
    }

    fun updateMetadata(metadata: MediaMetadataCompat) {
        this.mediaMetadata = metadata
        updateNotification()
    }

    private fun updateNotification() {
        if (!isStarted) {
            startNotification()
        } else {
            val notification = createNotification()
            if (notification != null) {
                manager.notify(NOTIFICATION_ID, notification)
            }
        }
    }

    fun stopNotification() {
        if (isStarted) {
            isStarted = false
            manager.cancel(NOTIFICATION_ID)
            service.stopForeground(true)
        }
    }

    fun pauseNotification() {
        if (isStarted) {
            service.stopForeground(false)
            manager.notify(NOTIFICATION_ID, createNotification())
            isStarted = false
        }
    }

    private fun contentIntent(context: Context): PendingIntent {
        val startActivityIntent = Intent(context, PlayerActivity::class.java)
        startActivityIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        return PendingIntent.getActivity(context,
                PLAYER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun pause(context: Context): Action {
        val playPauseIntent = Intent(context, MusicPlaybackService::class.java)
        playPauseIntent.action = ACTION_PAUSE

        val pausePendingIntent = PendingIntent.getService(context,
                PAUSE_PENDING_INTENT_ID,
                playPauseIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return Action(R.drawable.ic_pause_notif, "Pause", pausePendingIntent)
    }

    private fun next(context: Context): Action {
        val nextIntent = Intent(context, MusicPlaybackService::class.java)
        nextIntent.action = ACTION_NEXT
        val nextPendingIntent = PendingIntent.getService(context,
                PLAY_NEXT_PENDING_INTENT_ID,
                nextIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return Action(R.drawable.ic_skip_next_notif, "Next", nextPendingIntent)
    }

    private fun prev(context: Context): Action {
        val prevIntent = Intent(context, MusicPlaybackService::class.java)
        prevIntent.action = ACTION_PREV

        val prevPendingIntent = PendingIntent.getService(context,
                PLAY_PREV_PENDING_INTENT_ID,
                prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return Action(R.drawable.ic_skip_prev_notif, "Previous", prevPendingIntent)
    }

    private fun play(context: Context): Action {
        val prevIntent = Intent(context, MusicPlaybackService::class.java)
        prevIntent.action = ACTION_PLAY

        val prevPendingIntent = PendingIntent.getService(context,
                PLAY_PENDING_INTENT_ID,
                prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
        return Action(R.drawable.ic_play_notif, "Play", prevPendingIntent)
    }

    private fun dismissedNotification(context: Context): PendingIntent {
        val prevIntent = Intent(context, MusicPlaybackService::class.java)
        prevIntent.action = ACTION_STOP

        return PendingIntent.getService(context,
                STOP_PENDING_INTENT_ID,
                prevIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createNotification(): Notification? {
        if (mediaMetadata == null || playbackState == null) return null
        val builder = NotificationCompat.Builder(service)
        builder.setStyle(NotificationCompat.MediaStyle()
                .setMediaSession(token)
                .setShowActionsInCompactView(1))
                .setColor(Color.WHITE)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setUsesChronometer(true)
                .setDeleteIntent(dismissedNotification(service))
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentIntent(contentIntent(service))
                .setContentTitle(mediaMetadata!!.getString(MediaMetadataCompat.METADATA_KEY_ARTIST))
                .setContentText(mediaMetadata!!.getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE))
                .addAction(prev(service))
        if (playbackState!!.state == PlaybackStateCompat.STATE_PLAYING) {
            builder.addAction(pause(service))
        } else {
            builder.addAction(play(service))
        }
        builder.addAction(next(service))
        setNotificationPlaybackState(builder)
        loadImage(mediaMetadata!!.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI), builder)
        return builder.build()
    }

    private fun setNotificationPlaybackState(builder: NotificationCompat.Builder) {
        if (playbackState == null || !isStarted) {
            return
        }
        if (playbackState!!.state == PlaybackStateCompat.STATE_PLAYING && playbackState!!.position >= 0) {
            builder.setWhen(System.currentTimeMillis() - playbackState!!.position)
                    .setShowWhen(true)
                    .setUsesChronometer(true)
        } else {
            builder.setWhen(0)
                    .setShowWhen(false)
                    .setUsesChronometer(false)
        }
        builder.setOngoing(playbackState!!.state == PlaybackStateCompat.STATE_PLAYING)
    }

    private fun loadImage(imageUrl: String, builder: NotificationCompat.Builder) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(service)
                    .load(imageUrl)
                    .asBitmap()
                    .priority(Priority.IMMEDIATE)
                    .into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                            builder.setLargeIcon(resource)
                            manager.notify(NOTIFICATION_ID, builder.build())
                        }
                    })
        }
    }

    companion object {

        val NOTIFICATION_ID = 412

        private val PLAYER_PENDING_INTENT_ID = 3405
        private val PAUSE_PENDING_INTENT_ID = 3406
        private val PLAY_PENDING_INTENT_ID = 3407
        private val PLAY_NEXT_PENDING_INTENT_ID = 3408
        private val PLAY_PREV_PENDING_INTENT_ID = 3409
        private val STOP_PENDING_INTENT_ID = 3410
    }
}