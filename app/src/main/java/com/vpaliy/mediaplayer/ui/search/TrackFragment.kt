package com.vpaliy.mediaplayer.ui.search

import com.vpaliy.mediaplayer.di.Params
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.injectWith
import com.vpaliy.mediaplayer.ui.base.BaseAdapter
import com.vpaliy.mediaplayer.ui.home.TrackAdapter

class TrackFragment : SearchFragment<Track>() {
  override val presenter: SearchContract.Presenter<Track> by injectWith(Params.SEARCH)

  override val adapter: BaseAdapter<Track> by lazy {
    TrackAdapter(context!!, { navigator.navigate(activity!!, it) },
        { navigator.actions(activity!!, it) })
  }
}