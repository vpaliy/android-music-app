package com.vpaliy.mediaplayer.playback

import android.content.Context
import android.graphics.Bitmap
import android.os.SystemClock
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.vpaliy.mediaplayer.data.mapper.Mapper
import com.vpaliy.mediaplayer.domain.interactor.InsertInteractor
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.playback.Playback
import com.vpaliy.mediaplayer.domain.playback.QueueManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.vpaliy.mediaplayer.domain.playback.PlaybackScope

@PlaybackScope
class PlaybackManager @Inject
constructor(val playback: Playback,
            val context: Context,
            val saveInteractor: InsertInteractor<Track>,
            val mapper: Mapper<MediaMetadataCompat, Track>) : Playback.Callback {

    private var isRepeat: Boolean = false
    private var isShuffle: Boolean = false
    private var lastState: Int = 0
    var queueManager: QueueManager? = null
    val mediaSessionCallback=MediaSessionCallback()
    var updateListener: MetadataUpdateListener? = null
    var serviceCallback: PlaybackServiceCallback? = null

    init {
        playback.assignCallback(this)
    }

    fun handlePlayRequest(track: Track?) {
        track?.let {
            saveInteractor.insert({},{},it)
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
        queueManager?.let {
            val track = if (isRepeat) it.current() else it.next()
            if (isRepeat) {
                playback.invalidateCurrent()
            }else if(!it.hasNext()){
                playback.invalidateCurrent()
                updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
                serviceCallback?.onPlaybackStop()
                return
            }
            handlePlayRequest(track)
        }
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
            handlePlayRequest(if(position > 5)
                it.current() else it.previous())
        }
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
        updatePlaybackState(PlaybackStateCompat.STATE_PAUSED)
        serviceCallback?.onPlaybackStop()
    }

    fun requestUpdate(){
        updatePlaybackState(lastState)
        updateMetadata()
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
                val track=it.current()
                val result = MediaMetadataCompat.Builder(mapper.map(track))
                        .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, it.size().toLong())
                        .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, it.index.toLong()+1L)
                        .putLong(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, playback.position())
                updateListener?.onMetadataChanged(result.build())
                if(!track.artworkUrl.isNullOrEmpty()){
                    Glide.with(context)
                            .load(track.artworkUrl)
                            .asBitmap()
                            .into(object:SimpleTarget<Bitmap>(){
                                override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>?) {
                                    result.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART,resource)
                                    updateListener?.onMetadataChanged(result.build())
                                }
                            })
                }
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