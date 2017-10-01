package com.vpaliy.mediaplayer.playback

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.net.wifi.WifiManager
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.Timeline
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayback21 @Inject
constructor(context: Context,
            audioManager: AudioManager,
            wifiLock: WifiManager.WifiLock) :
        BasePlayback(context, audioManager, wifiLock), ExoPlayer.EventListener {

    private var exoPlayer: SimpleExoPlayer? = null
    private var isPause = false
    private var wasFocusLost=false

    override fun pausePlayer() {
        exoPlayer?.let {
            isPause=true
            it.playWhenReady=false
        }
    }

    override fun resumePlayer() {
        if (exoPlayer != null) {
            configPlayer()
        }
    }

    override fun updatePlayer(){
        if(wasFocusLost || isPause) configPlayer()
    }

    override fun startPlayer() {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayerFactory.newSimpleInstance(
                    context, DefaultTrackSelector(), DefaultLoadControl())
            exoPlayer?.addListener(this)
        }
        exoPlayer?.audioStreamType = AudioManager.STREAM_MUSIC
        val dataSourceFactory = DefaultDataSourceFactory(
                context, Util.getUserAgent(context, "uamp"), null)
        val extractorsFactory = DefaultExtractorsFactory()
        val mediaSource = ExtractorMediaSource(
                Uri.parse(currentUrl), dataSourceFactory, extractorsFactory, null, null)
        exoPlayer?.prepare(mediaSource)
        configPlayer()
    }

    private fun configPlayer() {
        wasFocusLost=false
        when (focusState) {
            BasePlayback.AUDIO_NO_FOCUS_NO_DUCK -> {
                wasFocusLost=true
                pause()
                return
            }
            BasePlayback.AUDIO_NO_FOCUS_CAN_DUCK -> exoPlayer?.volume = BasePlayback.VOLUME_DUCK
            BasePlayback.AUDIO_FOCUSED -> exoPlayer?.volume = BasePlayback.VOLUME_NORMAL
        }
        registerNoiseReceiver()
        isPause = false
        exoPlayer?.playWhenReady = true
    }

    override fun stopPlayer() {
        exoPlayer?.let {
            it.release()
            it.removeListener(this)
            exoPlayer=null
        }
    }

    override fun seekTo(position: Int) {
        exoPlayer?.let {
            registerNoiseReceiver()
            it.seekTo(position.toLong())
        }
    }

    override fun isPlaying()=exoPlayer?.playWhenReady?:false

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_READY ->   if (isPause) callback.onPause() else callback.onPlay()
            ExoPlayer.STATE_ENDED -> callback.onCompletetion()
        }
    }

    override fun onPlayerError(error: ExoPlaybackException)=callback.onError()

    override fun position()=exoPlayer?.currentPosition?:0

    override fun onTracksChanged(trackGroups: TrackGroupArray, trackSelections: TrackSelectionArray) {
    }

    override fun onPositionDiscontinuity() {
    }

    override fun onLoadingChanged(isLoading: Boolean) {
    }

    override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {
    }
}