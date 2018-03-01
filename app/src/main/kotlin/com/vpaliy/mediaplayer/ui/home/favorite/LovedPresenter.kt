package com.vpaliy.mediaplayer.ui.home.favorite

import com.vpaliy.mediaplayer.ui.home.HomePresenter
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.model.TrackType

@ViewScope
class LovedPresenter @Inject constructor(interactor: SingleInteractor<TrackType, List<Track>>, clear: ClearInteractor)
  : HomePresenter(interactor, clear) {
  override fun type() = TrackType.Favorite
}
