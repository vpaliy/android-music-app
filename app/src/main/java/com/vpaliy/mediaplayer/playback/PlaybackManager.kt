package com.vpaliy.mediaplayer.playback

import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.playback.Playback
import com.vpaliy.mediaplayer.domain.playback.QueueManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.vpaliy.mediaplayer.domain.playback.PlaybackScope

@PlaybackScope
class PlaybackManager @Inject
constructor(private val playback: Playback, private val mapper: Mapper<MediaMetadataCompat, Track>) : Playback.Callback {

    private var isRepeat: Boolean = false
    private var isShuffle: Boolean = false
    private var lastState: Int = 0
    private var queueManager: QueueManager? = null
    val mediaSessionCallback=MediaSessionCallback()
    var updateListener: MetadataUpdateListener? = null
    var serviceCallback: PlaybackServiceCallback? = null

    init {
        playback.assignCallback(this)
    }

    fun handlePlayRequest(track: Track?) {
        track?.let {
            playback.play(it.streamUrl)
            updateMetadata()
        }
    }

    private val availableActions: Long
        get() {
            var actions = PlaybackStateCompat.ACTION_PLAY_PAUSE or
                    PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID or
                    PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH or
                    PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS or
                    PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            if (playback.isPlaying()) {
                actions = actions or PlaybackStateCompat.ACTION_PAUSE
            } else {
                actions = actions or PlaybackStateCompat.ACTION_PLAY
            }
            if (isRepeat) {
                actions = actions or PlaybackStateCompat.ACTION_SET_REPEAT_MODE
            }
            if (isShuffle) {
                actions = actions or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE_ENABLED
            }
            return actions
        }

    fun handlePauseRequest() =playback.pause()

    fun handleStopRequest() {
        if (!playback.isPlaying()) {
            playback.stop()
        }
    }

    override fun onPlay() {
        updatePlaybackState(PlaybackStateCompat.STATE_PLAYING)
        serviceCallback?.onPlaybackStart()
    }

    override fun onStop() {
        updatePlaybackState(PlaybackStateCompat.STATE_STOPPED)
        serviceCallback?.onPlaybackStop()
    }

    override fun onError() {
        updateListener?.onMetadataRetrieveError()
    }

    override fun onCompletetion() {
        val track = if (isRepeat) queueManager?.current() else queueManager?.next()
        if (isRepeat) playback.invalidateCurrent()
        handlePlayRequest(track)
    }

    fun handleResumeRequest() {
        queueManager?.let { handlePlayRequest(it.current())}
    }

    fun handleNextRequest() {
        queueManager?.let {
            playback.invalidateCurrent()
            handlePlayRequest(it.next())
        }
    }

    fun handlePrevRequest() {
        queueManager?.let {
            val position = TimeUnit.MILLISECONDS.toSeconds(playback.position())
            playback.invalidateCurrent()
            handlePlayRequest(if(position > 5 || position <=2)
                it.current() else it.previous())
        }
    }

    fun setQueueManager(manager:QueueManager){
        queueManager=manager
    }

    private fun handleRepeatMode() {
        isRepeat = !isRepeat
        updatePlaybackState(lastState)
    }

    private fun handleShuffleMode() {
        isShuffle = !isShuffle
        queueManager?.shuffle()
        updatePlaybackState(lastState)
    }

    override fun onPause() {
        Log.d("Music:","onPause()")
        updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
        serviceCallback?.onPlaybackStop()
    }

    fun updatePlaybackState(state: Int) {
        val position = playback.position()
        this.lastState = state
        if (state == PlaybackStateCompat.STATE_PLAYING || state == PlaybackStateCompat.STATE_PAUSED) {
            serviceCallback?.onNotificationRequired()
        }
        val builder = PlaybackStateCompat.Builder()
                .setActions(availableActions)
                .setState(state, position, 1.0f, SystemClock.elapsedRealtime())
        serviceCallback?.onPlaybackStateUpdated(builder.build())
    }

    private fun updateMetadata() {
        if (updateListener != null) {
            queueManager?.let {
                val result = MediaMetadataCompat.Builder(mapper.map(it.current()))
                        .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, it.size().toLong())
                        .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, 10)
                        .putLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, playback.position())
                        .build()
                updateListener?.onMetadataChanged(result)
            }
        }
    }

    inner class MediaSessionCallback : MediaSessionCompat.Callback() {
        override fun onPlay() {
            super.onPlay()
            handlePlayRequest(queueManager?.current())
        }

        override fun onSkipToNext() {
            super.onSkipToNext()
            handleNextRequest()
        }

        override fun onSkipToPrevious() {
            super.onSkipToPrevious()
            handlePrevRequest()
        }

        override fun onPause() {
            super.onPause()
            handlePauseRequest()
        }

        override fun onSetRepeatMode(repeatMode: Int) {
            handleRepeatMode()
        }

        override fun onSetShuffleModeEnabled(enabled: Boolean) {
            super.onSetShuffleModeEnabled(enabled)
            handleShuffleMode()
        }

        override fun onStop() {
            super.onStop()
            handleStopRequest()
        }

        override fun onSeekTo(pos: Long) {
            super.onSeekTo(pos)
            playback.seekTo(pos.toInt())
        }
    }

    interface MetadataUpdateListener {
        fun onMetadataChanged(metadata: MediaMetadataCompat)
        fun onMetadataRetrieveError()
    }

    interface PlaybackServiceCallback {
        fun onPlaybackStart()
        fun onPlaybackStop()
        fun onNotificationRequired()
        fun onPlaybackStateUpdated(newState: PlaybackStateCompat)
    }
}