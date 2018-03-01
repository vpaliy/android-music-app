package com.vpaliy.mediaplayer.ui.player

object PlayerContract {
  interface View {
    fun showControls()
    fun hideControls()
    fun playPause(play: Boolean)
    fun showArt(url: String)
    fun enableNext()
    fun disableNext()
    fun enablePrev()
    fun disablePrev()
    fun setDuration()
  }

  interface Presenter {
    fun seekTo(current:Long)
    fun onNextClicked()
    fun onPrevClicked()
    fun onPlayClicked()
  }
}