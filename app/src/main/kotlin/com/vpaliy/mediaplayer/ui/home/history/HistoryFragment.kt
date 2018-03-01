package com.vpaliy.mediaplayer.ui.home.history

import com.vpaliy.mediaplayer.App
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.di.qualifier.History
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment
import javax.inject.Inject

class HistoryFragment : HomeFragment() {
  override var presenter: Presenter? = null
    @Inject set(@History value) {
      field = value
      field?.attach(this)
    }

  override fun inject() {
    DaggerViewComponent.builder()
        .applicationComponent(App.component)
        .presenterModule(PresenterModule())
        .build().inject(this)
  }

  override fun attach(presenter: Presenter) {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  override fun emptyMessage(): Int = R.string.empty_history
  override fun alertMessage(): String = getString(R.string.history_alert)
}
