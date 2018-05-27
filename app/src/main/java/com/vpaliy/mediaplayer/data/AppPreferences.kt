package com.vpaliy.mediaplayer.data

import com.vpaliy.mediaplayer.domain.interactor.PreferencesInteractor
import com.vpaliy.mediaplayer.domain.model.Average
import com.vpaliy.mediaplayer.domain.model.ImageQuality

class AppPreferences : PreferencesInteractor {
  override fun saveImageQuality(quality: ImageQuality) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }

  override fun getImageQuality(): ImageQuality {
    //TODO needs implementation
    return Average
  }
}