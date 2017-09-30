package com.vpaliy.mediaplayer.ui.home.loved

import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import com.vpaliy.mediaplayer.ui.home.HomePresenter
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.scope.ViewScope

@ViewScope
class LovedPresenter @Inject
constructor(interactor:LovedTracks) : HomePresenter(interactor,interactor)
