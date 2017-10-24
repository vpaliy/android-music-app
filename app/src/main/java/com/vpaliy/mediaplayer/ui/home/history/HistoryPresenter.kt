package com.vpaliy.mediaplayer.ui.home.history

import com.vpaliy.mediaplayer.ui.home.HomePresenter
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.domain.interactor.ClearInteractor
import com.vpaliy.mediaplayer.domain.interactor.SingleInteractor
import com.vpaliy.mediaplayer.domain.model.TrackType

@ViewScope
class HistoryPresenter @Inject
constructor(interactor: SingleInteractor<TrackType>, clear: ClearInteractor): HomePresenter(interactor,clear) {
    override fun type() = TrackType.HISTORY
}