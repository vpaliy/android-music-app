package com.vpaliy.mediaplayer.di.module

import com.vpaliy.mediaplayer.di.qualifier.History
import com.vpaliy.mediaplayer.di.qualifier.Loved
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.domain.interactor.LovedTracks
import com.vpaliy.mediaplayer.domain.interactor.TrackHistory
import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.history.HistoryPresenter
import com.vpaliy.mediaplayer.ui.home.loved.LovedPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule{

    @ViewScope
    @Provides
    @History
    fun history(interactor:TrackHistory):HomeContract.Presenter= HistoryPresenter(interactor)

    @ViewScope
    @Provides
    @Loved
    fun loved(interactor:LovedTracks):HomeContract.Presenter= LovedPresenter(interactor)
}
