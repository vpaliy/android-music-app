package com.vpaliy.mediaplayer.ui.player

import android.app.Activity
import android.content.ComponentName
import android.os.RemoteException
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.vpaliy.mediaplayer.playback.MusicPlaybackService

class PlayerController(val activity: Activity) {

  private lateinit var callback:Callback

  private val controllerCallback = object : MediaControllerCompat.Callback() {
    override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
      // updatePlaybackState(state)
      when (state?.state){
        PlaybackStateCompat.STATE_PLAYING -> callback.onPlayed()
        PlaybackStateCompat.STATE_PAUSED -> callback.onPaused()
      }
    }

    override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
      // updateDuration(metadata)
      //updatePicture(metadata)
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
    MediaBrowserCompat(activity, ComponentName(activity, MusicPlaybackService::class.java),
        connectionCallback, null)
  }

  fun onStart() {
    browser.connect()
  }

  fun onStop() {
    browser.disconnect()
    MediaControllerCompat.getMediaController(activity)
        .unregisterCallback(controllerCallback)
  }

  fun seekTo(position: Long) {
    controls().seekTo(position)
  }

  fun play(){
    controls().play()
  }

  fun pause(){
    controls().pause()
  }

  fun playNext(){
    controls().skipToNext()
  }

  fun playPrevious(){
    controls().skipToPrevious()
  }

  private fun controls() = MediaControllerCompat
      .getMediaController(activity)
      .transportControls

  interface Callback {
    fun onPlayed()
    fun onPaused()
  }
}