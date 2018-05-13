package com.vpaliy.mediaplayer.playback

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.PowerManager
import android.support.v4.media.session.PlaybackStateCompat
import java.io.IOException

class MediaPlayback(
    context: Context,
    audioManager: AudioManager,
    wifiLock: WifiManager.WifiLock) :
    BasePlayback(context, audioManager, wifiLock),
    MediaPlayer.OnPreparedListener,
    MediaPlayer.OnErrorListener,
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnSeekCompleteListener {

  private var player: MediaPlayer? = null
  private var playerState = PlaybackStateCompat.STATE_NONE

  override fun startPlayer() {
    createPlayerIfNeeded()
    try {
      playerState = PlaybackStateCompat.STATE_BUFFERING
      player?.let {
        it.setAudioStreamType(AudioManager.STREAM_MUSIC)
        it.setDataSource(currentUrl)
        it.prepareAsync()
      }
    } catch (ex: IOException) {
      ex.printStackTrace()
      callback.onError()
    }

  }

  override fun updatePlayer() {
    player?.let {
      when (focusState) {
        BasePlayback.AUDIO_NO_FOCUS_NO_DUCK -> {
          if (playerState == PlaybackStateCompat.STATE_PLAYING) {
            pause()
          }
          return
        }
        BasePlayback.AUDIO_NO_FOCUS_CAN_DUCK -> {
          registerNoiseReceiver()
          it.setVolume(BasePlayback.VOLUME_DUCK, BasePlayback.VOLUME_DUCK)
        }
        else -> {
          registerNoiseReceiver()
          it.setVolume(BasePlayback.VOLUME_NORMAL, BasePlayback.VOLUME_NORMAL)
        }
      }
      it.start()
      playerState = PlaybackStateCompat.STATE_PLAYING
      callback.onPlay()
    }
  }

  override fun onPrepared(mp: MediaPlayer) {
    updatePlayer()
    playerState = PlaybackStateCompat.STATE_PLAYING
  }

  private fun createPlayerIfNeeded() {
    if (player == null) {
      player = MediaPlayer()
      player?.let {
        it.setWakeMode(context.applicationContext,
            PowerManager.PARTIAL_WAKE_LOCK)
        it.setOnPreparedListener(this)
        it.setOnErrorListener(this)
        it.setOnCompletionListener(this)
        it.setOnSeekCompleteListener(this)
      }
    } else {
      player?.reset()
    }
  }

  override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
    callback.onError()
    stopPlayer()
    return true
  }

  override fun pausePlayer() {
    if (playerState == PlaybackStateCompat.STATE_PLAYING) {
      if (isPlaying()) {
        player?.pause()
      }
    }
    playerState = PlaybackStateCompat.STATE_PAUSED
  }

  override fun onSeekComplete(mp: MediaPlayer) {
    if (playerState == PlaybackStateCompat.STATE_BUFFERING) {
      registerNoiseReceiver()
      player?.start()
      playerState = PlaybackStateCompat.STATE_PLAYING
    }
  }

  override fun onCompletion(mp: MediaPlayer) {
    callback.onCompleted()
  }

  override fun resumePlayer() {
    player?.let {
      if (it.isPlaying) it.start()
      playerState = PlaybackStateCompat.STATE_PLAYING
      callback.onPlay()
    }
  }

  override fun stopPlayer() {
    playerState = PlaybackStateCompat.STATE_STOPPED
    player?.let {
      it.release()
      player = null
    }
  }

  override fun seekTo(position: Int) {
    player?.let {
      if (it.isPlaying) playerState = PlaybackStateCompat.STATE_BUFFERING
      registerNoiseReceiver()
      it.seekTo(position)
    }
  }

  override fun position() = player?.currentPosition?.toLong() ?: 0L

  override fun isPlaying() = player?.isPlaying ?: false
}