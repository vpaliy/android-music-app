package com.vpaliy.mediaplayer.ui.search

import com.vpaliy.mediaplayer.App
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.Navigator
import com.vpaliy.mediaplayer.ui.home.TrackAdapter
import javax.inject.Inject

class TrackFragment : SearchFragment<Track>() {
  override var presenter: SearchContract.Presenter<Track>? = null
    @Inject set(value) {
      field = value
      field?.attachView(this)
    }

  @Inject lateinit var navigator: Navigator

  override val adapter: BaseAdapter<Track> by lazy(LazyThreadSafetyMode.NONE) {
    TrackAdapter(context, { navigator.navigate(activity, it) }, { navigator.actions(activity, it) })
  }

  override fun inject() {
    DaggerViewComponent.builder()
        .applicationComponent(App.component)
        .presenterModule(PresenterModule())
        .build().inject(this)
  }
}