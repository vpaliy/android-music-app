package com.vpaliy.mediaplayer.di.module

import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.history.HistoryPresenter
import com.vpaliy.mediaplayer.ui.home.favorite.LovedPresenter
import com.vpaliy.mediaplayer.domain.interactor.*
import com.vpaliy.mediaplayer.ui.details.ActionsContract
import com.vpaliy.mediaplayer.ui.details.ActionsPresenter
import com.vpaliy.mediaplayer.ui.search.SearchContract
import com.vpaliy.mediaplayer.di.qualifier.History
import com.vpaliy.mediaplayer.di.qualifier.Loved
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.search.TrackPresenter
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {
  @ViewScope
  @History
  @Provides
  internal fun history(tracks: GetTracks, modify: ModifyTracks)
      : HomeContract.Presenter
      = HistoryPresenter(tracks, modify)

  @ViewScope
  @Loved
  @Provides
  internal fun loved(tracks: GetTracks, modify: ModifyTracks)
      : HomeContract.Presenter
      = LovedPresenter(tracks, modify)

  @ViewScope
  @Provides
  internal fun search(interactor: SearchTracks)
      : SearchContract.Presenter<Track>
      = TrackPresenter(interactor)

  @ViewScope
  @Provides
  internal fun actions(modify: ModifyTracks)
      : ActionsContract.Presenter
      = ActionsPresenter(modify)
}
