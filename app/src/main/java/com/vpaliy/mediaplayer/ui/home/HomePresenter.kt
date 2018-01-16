package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.home.HomeContract.*
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.domain.interactor.params.Consumer
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyParam
import com.vpaliy.mediaplayer.domain.interactor.params.Response
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.then

@ViewScope
abstract class HomePresenter constructor(val interactor: SingleInteractor<TrackType>,
                                         val clear: ClearInteractor) : Presenter {

  protected lateinit var view: View

  override fun start() {
    view.setLoading(true)
    interactor.execute(Consumer(this::onSuccess, this::onError))
  }

  private fun onSuccess(response: Response<TrackType>) {
    view.setLoading(false)
    response.result.isEmpty().then(view::empty, { view.show(response.result) })
  }

  protected fun onError(error: Throwable) {
    error.printStackTrace()
    view.setLoading(false)
    view.error()
  }

  override fun attach(view: View) {
    this.view = view
  }

  override fun remove(track: Track) {
    clear.remove({ view.removed(track) }, this::onError, ModifyParam(track, type()))
  }

  override fun clear() {
    clear.clearAll(view::cleared, this::onError, type())
  }

  override fun stop() {} //interactor.dispose()

  protected abstract fun type(): TrackType
}
