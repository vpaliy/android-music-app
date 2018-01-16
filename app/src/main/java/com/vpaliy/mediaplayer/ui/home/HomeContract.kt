package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BasePresenter
import com.vpaliy.mediaplayer.ui.base.BaseView

interface HomeContract {
  interface View : BaseView<Presenter> {
    fun show(list: List<Track>)
    fun error()
    fun empty()
    fun cleared()
    fun removed(track: Track)
    fun setLoading(isLoading: Boolean)
  }

  interface Presenter : BasePresenter {
    fun start()
    override fun stop()
    fun remove(track: Track)
    fun clear()
    fun attach(view: View)
  }
}
