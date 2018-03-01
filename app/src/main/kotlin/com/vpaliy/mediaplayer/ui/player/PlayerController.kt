package com.vpaliy.mediaplayer.ui.player

import android.app.Activity
import android.content.ComponentName
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.playback.Playback
import com.vpaliy.mediaplayer.domain.playback.QueueManager
import com.vpaliy.mediaplayer.playback.MusicPlaybackService

class PlayerController(val activity: Activity, val playback: Playback) : Playback.Callback {

  private lateinit var callback: Callback
  private var queueManager: QueueManager? = null
  private var serviceCallback: PlaybackServiceCallback? = null
  private var isShuffle = false
  private var isRepeat = false

  init {
    playback.assignCallback(callback = this)
  }

  private val controllerCallback = object : MediaControllerCompat.Callback() {
    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
      when (state?.state) {
        PlaybackStateCompat.STATE_PLAYING -> callback.onPlayed()
        PlaybackStateCompat.STATE_PAUSED -> callback.onPaused()
      }
    }


    override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
      metadata?.let {
        callback.updateMetadata(it)
      }
    }
  }

  private val connectionCallback: MediaBrowserCompat.ConnectionCallback by lazy {
    object : MediaBrowserCompat.ConnectionCallback() {
      override fun onConnected() {
        super.onConnected()
        try {
          val mediaController = MediaControllerCompat(activity, browser.sessionToken)
          mediaController.registerCallback(controllerCallback)
          MediaControllerCompat.setMediaController(activity, mediaController)
        } catch (ex: RemoteException) {
          ex.printStackTrace()
        }
      }
    }
  }

  private val browser by lazy {
    MediaBrowserCompat(activity, ComponentName(activity, MusicPlaybackService::class.java), connectionCallback, null)
  }

  /* UI Logic */
  fun start() {
    browser.connect()
  }

  fun stop() {
    browser.disconnect()
    MediaControllerCompat.getMediaController(activity).unregisterCallback(controllerCallback)
  }

  fun seekTo(position: Long) {
    controls().seekTo(position)
  }

  fun play() {
    controls().play()
  }

  fun pause() {
    controls().pause()
  }

  fun playNext() {
    controls().skipToNext()
  }

  fun playPrevious() {
    controls().skipToPrevious()
  }

  /* Playback logic  */
  override fun onCompleted() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onError() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onPause() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onPlay() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun onStop() {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  private fun handlePlayRequest(track:Track?){

  }

  private fun handleNextRequest(){

  }

  private fun handlePrevRequest(){

  }

  private fun handleRepeatMode(){

  }

  private fun handleStopRequest(){

  }

  private fun handlePauseRequest(){

  }

  private fun handleShuffleMode(){

  }

  inner class MediaSessionCallback : MediaSessionCompat.Callback() {
    override fun onPlay() {
      handlePlayRequest(queueManager?.current())
    }

    override fun onSkipToNext() {
      handleNextRequest()
    }

    override fun onSkipToPrevious() {
      handlePrevRequest()
    }

    override fun onPause() {
      super.onPause()
      handlePauseRequest()
    }

    override fun onSetRepeatMode(repeatMode: Int) {
      handleRepeatMode()
    }

    override fun onSetShuffleMode(shuffleMode: Int) {
      handleShuffleMode()
    }

    override fun onStop() {
      handleStopRequest()
    }

    override fun onSeekTo(pos: Long) {
      playback.seekTo(pos.toInt())
    }
  }

  private fun controls() = MediaControllerCompat
      .getMediaController(activity)
      .transportControls

  interface Callback {
    fun onPlayed()
    fun onPaused()
    fun updateMetadata(media: MediaMetadataCompat)
  }

  interface PlaybackServiceCallback
}