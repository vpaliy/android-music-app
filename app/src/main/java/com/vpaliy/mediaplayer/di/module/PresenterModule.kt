package com.vpaliy.mediaplayer.di.module

import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.history.HistoryPresenter
import com.vpaliy.mediaplayer.ui.home.loved.LovedPresenter
import com.vpaliy.mediaplayer.domain.interactor.*
import com.vpaliy.mediaplayer.ui.details.ActionsContract
import com.vpaliy.mediaplayer.ui.details.ActionsPresenter
import com.vpaliy.mediaplayer.ui.search.SearchContract
import com.vpaliy.mediaplayer.ui.search.SearchPresenter
import com.vpaliy.mediaplayer.di.qualifier.History
import com.vpaliy.mediaplayer.di.qualifier.Loved
import com.vpaliy.mediaplayer.di.scope.ViewScope
import dagger.Module
import dagger.Provides

@Module
class PresenterModule{
    @ViewScope
    @History
    @Provides
    internal fun history(tracks:GetTracks,modify: ModifyTracks)
            :HomeContract.Presenter
            =HistoryPresenter(tracks,modify)

    @ViewScope
    @Loved
    @Provides
    internal fun loved(tracks:GetTracks,modify: ModifyTracks)
            :HomeContract.Presenter
            =LovedPresenter(tracks,modify)

    @ViewScope
    @Provides
    internal fun search(search:SearchTracks)
            :SearchContract.Presenter
            =SearchPresenter(search)

    @ViewScope
    @Provides
    internal fun actions(modify: ModifyTracks)
            :ActionsContract.Presenter
            =ActionsPresenter(modify)
}
