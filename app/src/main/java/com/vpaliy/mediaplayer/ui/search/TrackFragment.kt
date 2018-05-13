package com.vpaliy.mediaplayer.ui.search

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.base.Navigator
import com.vpaliy.mediaplayer.ui.home.TrackAdapter
import org.koin.android.ext.android.inject

class TrackFragment : SearchFragment<Track>() {
  override var presenter: SearchContract.Presenter<Track>? = null

  private val navigator: Navigator by inject()

  override val adapter: BaseAdapter<Track> by lazy(LazyThreadSafetyMode.NONE) {
    TrackAdapter(context, { navigator.navigate(activity, it) }, { navigator.actions(activity, it) })
  }
}