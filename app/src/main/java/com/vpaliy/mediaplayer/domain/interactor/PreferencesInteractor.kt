package com.vpaliy.mediaplayer.domain.interactor

import com.vpaliy.mediaplayer.domain.model.ImageQuality

interface PreferencesInteractor {
  fun saveImageQuality(quality: ImageQuality)
  fun getImageQuality(): ImageQuality
}