package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.home.HomeContract.*
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.TrackType

@ViewScope
abstract class HomePresenter constructor(val interactor: SingleInteractor<TrackType, List<Track>>,
                                         val clear: ClearInteractor) : Presenter {

  protected lateinit var view: View

  override fun start() {
    view.setLoading(true)
    interactor.execute(this::onSuccess, this::onError, type())
  }

  override fun refresh() {
    start()
  }

  private fun onSuccess(response: List<Track>) {
    view.show(response)
    view.setLoading(false)
  }

  private fun onError(error: Throwable) {
    error.printStackTrace()
    view.setLoading(false)
    view.error()
  }

  override fun attach(view: View) {
    this.view = view
  }

  override fun remove(track: Track) {
    clear.remove({ view.removed(track) }, this::onError, ModifyRequest(type(), track))
  }

  override fun clear() {
    clear.clearAll(view::cleared, this::onError, type())
  }

  protected abstract fun type(): TrackType
}
