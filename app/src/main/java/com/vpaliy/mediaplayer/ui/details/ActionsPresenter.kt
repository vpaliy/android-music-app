package com.vpaliy.mediaplayer.ui.details

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.details.ActionsContract.Presenter
import com.vpaliy.mediaplayer.domain.interactor.ModifyInteractor
import com.vpaliy.mediaplayer.domain.interactor.params.ModifyRequest
import com.vpaliy.mediaplayer.domain.model.TrackType

class ActionsPresenter (
    private val modifier: ModifyInteractor) : Presenter {

  private lateinit var view: ActionsContract.View

  override fun add(track: Track) {
    val param = ModifyRequest(TrackType.History, track)
    modifier.insert({ view.showAdded(TrackType.History) }, this::error, param)
  }

  override fun remove(track: Track) {
    val param = ModifyRequest(TrackType.History, track)
    modifier.remove({ view.showRemoved(TrackType.History) }, this::error, param)
  }

  override fun dislike(track: Track) {
    val param = ModifyRequest(TrackType.Favorite, track)
    modifier.remove({ view.showRemoved(TrackType.Favorite) }, this::error, param)
  }

  override fun like(track: Track) {
    val param = ModifyRequest(TrackType.Favorite, track)
    modifier.insert({ view.showAdded(TrackType.Favorite) }, this::error, param)
  }

  private fun error(ex: Throwable) {
    view.error()
    ex.printStackTrace()
  }

  override fun attach(view: ActionsContract.View) {
    this.view = view
  }
}