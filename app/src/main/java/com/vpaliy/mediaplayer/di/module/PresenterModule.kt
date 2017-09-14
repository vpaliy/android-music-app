package com.vpaliy.mediaplayer.di.module

import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import com.vpaliy.mediaplayer.domain.interactor.TrackHistory
import com.vpaliy.mediaplayer.ui.home.history.HistoryPresenter
import com.vpaliy.mediaplayer.ui.home.loved.LovedPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule{

    @ViewScope
    @Provides
    fun history(interactor:TrackHistory)= HistoryPresenter(interactor)

    @ViewScope
    @Provides
    fun loved(interactor:LovedTracks)= LovedPresenter(interactor)
}
