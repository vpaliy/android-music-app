package com.vpaliy.mediaplayer.playback

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.net.wifi.WifiManager
import com.vpaliy.mediaplayer.domain.playback.Playback

abstract class BasePlayback(protected val context: Context,
                            protected val audioManager: AudioManager,
                            protected val wifiLock: WifiManager.WifiLock) :
        Playback, AudioManager.OnAudioFocusChangeListener {

    protected lateinit var callback:Playback.Callback
    protected var focusState = AUDIO_NO_FOCUS_NO_DUCK
    private var noisyReceiverRegistered=false
    @Volatile protected var currentUrl: String? = null

    private val audioBecomingNoisyIntent = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)

    private val audioNoisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY == intent.action) {
                if (isPlaying()) {
                }
            }
        }
    }

    override fun play(streamUrl: String?) {
        if(!streamUrl.isNullOrEmpty()){
            requestFocus()
            registerNoiseReceiver()
            acquireWifiLock()
            if(streamUrl.equals(currentUrl)){
                resumePlayer()
                return
            }
            this.currentUrl = streamUrl
            startPlayer()
        }
    }

    abstract fun startPlayer()
    abstract fun stopPlayer()
    abstract fun pausePlayer()
    abstract fun resumePlayer()
    abstract fun updatePlayer()

    protected fun acquireWifiLock()=wifiLock.acquire()

    protected fun requestFocus() {
        val result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN)
        focusState = when (result) {
            AudioManager.AUDIOFOCUS_REQUEST_GRANTED -> AUDIO_FOCUSED
            else -> AUDIO_NO_FOCUS_NO_DUCK
        }
    }

    override fun invalidateCurrent() {
        currentUrl = null
    }

    override fun onAudioFocusChange(focusChange: Int) {
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            focusState = AUDIO_FOCUSED
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            val canDuck = focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK
            focusState = if (canDuck) AUDIO_NO_FOCUS_CAN_DUCK else AUDIO_NO_FOCUS_NO_DUCK
        }
        updatePlayer()
    }

    private fun releaseFocus() {
        audioManager.abandonAudioFocus(this)
        focusState = AUDIO_NO_FOCUS_NO_DUCK
    }

    private fun releaseWifiLock() {
        if (wifiLock.isHeld) {
            wifiLock.release()
        }
    }

    protected fun registerNoiseReceiver() {
        if (!noisyReceiverRegistered) {
            context.registerReceiver(audioNoisyReceiver, audioBecomingNoisyIntent)
            noisyReceiverRegistered = true
        }
    }

    protected fun unregisterNoiseReceiver() {
        if (noisyReceiverRegistered) {
            context.unregisterReceiver(audioNoisyReceiver)
            noisyReceiverRegistered = false
        }
    }

    override fun pause() {
        pausePlayer()
        unregisterNoiseReceiver()
        releaseWifiLock()
    }

    override fun stop() {
        currentUrl = null
        releaseFocus()
        releaseWifiLock()
        unregisterNoiseReceiver()
        stopPlayer()
        callback.onStop()
    }

    companion object {

        val VOLUME_DUCK = 0.2f
        val VOLUME_NORMAL = 1.0f

        val AUDIO_NO_FOCUS_NO_DUCK = 0
        val AUDIO_NO_FOCUS_CAN_DUCK = 1
        val AUDIO_FOCUSED = 2
    }
}