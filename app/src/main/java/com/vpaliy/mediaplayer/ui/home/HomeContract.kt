package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BasePresenter
import com.vpaliy.mediaplayer.ui.base.BaseView

interface HomeContract {
  interface View : BaseView<Presenter> {
    fun showTracks(list: List<Track>)
    fun error()
    fun showEmpty()
    fun showCleared()
    fun removed(track: Track)
    fun showLoading()
    fun hideLoading()
  }

  interface Presenter : BasePresenter {
    fun start()
    fun remove(track: Track)
    fun clearAll()
    fun refresh()
  }
}
