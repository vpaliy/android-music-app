package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.home.HomeContract.*
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.TrackType

abstract class HomePresenter(
    protected val interactor: SingleInteractor<TrackType, List<Track>>,
    protected val clear: ClearInteractor,
    protected val view: HomeContract.View
) : Presenter {

  override fun start() {
    view.showLoading()
    interactor.execute(this::onSuccess, this::onError, type())
  }

  override fun refresh() {
    start()
  }

  private fun onSuccess(response: List<Track>) {
    view.show(response)
    view.hideLoading()
  }

  private fun onError(error: Throwable) {
    error.printStackTrace()
    view.hideLoading()
    view.error()
  }

  override fun remove(track: Track) {
    clear.remove({ view.removed(track) }, this::onError, ModifyRequest(type(), track))
  }

  override fun clear() {
    clear.clearAll(view::cleared, this::onError, type())
  }

  protected abstract fun type(): TrackType
}
