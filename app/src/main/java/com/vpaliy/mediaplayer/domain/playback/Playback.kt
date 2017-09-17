package com.vpaliy.mediaplayer.domain.playback

interface Playback {

    fun play(streamUrl: String?)
    fun pause()
    fun stop()
    fun seekTo(position: Int)
    fun invalidateCurrent()
    fun isPlaying(): Boolean
    fun position(): Long

    interface Callback {
        fun onCompletetion()
        fun onPause()
        fun onPlay()
        fun onStop()
        fun onError()
    }
}