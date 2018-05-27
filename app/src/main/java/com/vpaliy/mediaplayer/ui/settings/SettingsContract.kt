package com.vpaliy.mediaplayer.ui.settings

import com.vpaliy.mediaplayer.domain.model.ImageQuality

object SettingsContract {
  interface Presenter {
    fun clearData()
    fun changeArtQuality(quality: ImageQuality)
    fun requestArtQuality()
  }

  interface View {
    fun showArtQuality(quality: ImageQuality)
    fun showDataCleared()
    fun showError()
  }
}