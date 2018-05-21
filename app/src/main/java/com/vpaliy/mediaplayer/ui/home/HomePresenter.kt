package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.ui.home.HomeContract.*
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.*

abstract class HomePresenter(
    protected val interactor: SingleInteractor<TrackType, List<Track>>,
    protected val clear: ClearInteractor,
    protected val view: HomeContract.View
) : Presenter {

  protected abstract val trackType: TrackType

  override fun start() {
    refresh()
  }

  override fun refresh() {
    view.showLoading()
    interactor.execute(this::onSuccess,
        this::onError, trackType)
  }

  private fun onSuccess(response: List<Track>) {
    view.hideLoading()
    if (response.isEmpty())
      view.showEmpty()
    else
      view.showTracks(response)
  }

  private fun onError(error: RequestError) {
    view.hideLoading()
    when (error) {
      Connection -> view.onConnectionError()
      FailedRequest -> view.error()
    }
  }

  override fun remove(track: Track) {
    clear.remove({ view.removed(track) },
        this::onError, ModifyRequest(trackType, track))
  }

  override fun clearAll() {
    clear.clearAll(view::showCleared,
        this::onError, trackType)
  }
}
