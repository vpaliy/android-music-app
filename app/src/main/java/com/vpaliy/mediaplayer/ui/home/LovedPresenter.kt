package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LovedPresenter @Inject constructor(interactor:LovedTracks):HomePresenter(interactor)
