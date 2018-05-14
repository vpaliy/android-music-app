package com.vpaliy.mediaplayer.ui.home.history

import com.vpaliy.mediaplayer.ui.home.HomePresenter
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.domain.model.TrackType
import com.vpaliy.mediaplayer.ui.home.HomeContract

class HistoryPresenter(
    interactor: SingleInteractor<TrackType, List<Track>>,
    clear: ClearInteractor,
    view : HomeContract.View
) : HomePresenter(interactor, clear, view) {
  override fun type() = TrackType.History
}