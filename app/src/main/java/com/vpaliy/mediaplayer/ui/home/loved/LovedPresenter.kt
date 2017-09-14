package com.vpaliy.mediaplayer.ui.home.loved

import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.ui.home.HomePresenter

@ViewScope
class LovedPresenter @Inject constructor(interactor:LovedTracks): HomePresenter(interactor)
