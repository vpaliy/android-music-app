package com.vpaliy.mediaplayer.ui.search

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BasePresenter
import com.vpaliy.mediaplayer.ui.base.BaseView

interface SearchContract {
  interface View : BaseView<Presenter> {
    fun setLoading(isLoading: Boolean)
    fun show(list: List<Track>)
    fun append(list: List<Track>)
    fun error()
    fun empty()
  }

  interface Presenter : BasePresenter {
    fun query(query: String?)
    fun more()
    fun attachView(view: View)
  }
}