package com.vpaliy.mediaplayer.ui.home.history

import com.vpaliy.mediaplayer.domain.interactor.TrackHistory
import com.vpaliy.mediaplayer.ui.home.HomePresenter
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope

@ViewScope
class HistoryPresenter @Inject
constructor(interactor:TrackHistory): HomePresenter(interactor,interactor)