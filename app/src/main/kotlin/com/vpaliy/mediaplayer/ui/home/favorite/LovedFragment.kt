package com.vpaliy.mediaplayer.ui.home.favorite

import com.vpaliy.mediaplayer.App
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.di.qualifier.Loved
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment
import javax.inject.Inject

class LovedFragment : HomeFragment() {
  override var presenter: Presenter? = null
    @Inject set(@Loved value) {
      field = value
      field?.attach(this)
    }

  override fun inject() {
    DaggerViewComponent.builder()
        .applicationComponent(App.component)
        .presenterModule(PresenterModule())
        .build().inject(this)
  }

  override fun emptyMessage(): Int = R.string.empty_liked
  override fun alertMessage(): String = getString(R.string.loved_alert)

  override fun attach(presenter: Presenter) {
  }
}
