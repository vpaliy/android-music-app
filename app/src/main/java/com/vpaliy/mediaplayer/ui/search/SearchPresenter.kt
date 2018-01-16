package com.vpaliy.mediaplayer.ui.search

import com.vpaliy.mediaplayer.ui.search.SearchContract.*
import com.vpaliy.mediaplayer.domain.model.SearchPage
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.model.Track
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope

@ViewScope
class SearchPresenter @Inject constructor(val search: SingleInteractor<SearchPage, List<Track>>) : Presenter<Track> {

  private lateinit var view: View<Track>
  private var page = SearchPage(1)

  override fun query(query: String?) {
    view.showLoading()
    page.query = query
    search.execute(this::onSuccess, this::onError, page)
  }

  override fun more() {
    view.showLoading()
    page.current++
    search.execute(this::append, this::onError, page)
  }

  override fun attachView(view: View<Track>) {
    this.view = view
  }

  private fun onSuccess(result:List<Track>) {
    view.hideLoading()
  }

  private fun append(result: List<Track>) {
    view.hideLoading()
    view.append(result)
  }

  private fun onError(error: Throwable) {
    error.printStackTrace()
    view.hideLoading()
    view.error()
  }
}