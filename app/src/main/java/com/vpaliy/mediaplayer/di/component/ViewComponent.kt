package com.vpaliy.mediaplayer.di.component

import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.ui.home.history.HistoryFragment
import com.vpaliy.mediaplayer.ui.home.favorite.LovedFragment
import com.vpaliy.mediaplayer.ui.search.SearchActivity
import com.vpaliy.mediaplayer.ui.details.ActionsActivity
import com.vpaliy.mediaplayer.ui.search.TrackFragment
import dagger.Component
import com.vpaliy.mediaplayer.di.scope.ViewScope

@ViewScope
@Component(dependencies = [(ApplicationComponent::class)],
    modules = [(PresenterModule::class)])
interface ViewComponent {
  fun inject(fragment: LovedFragment)
  fun inject(fragment: HistoryFragment)
  fun inject(fragment: TrackFragment)
  fun inject(activity: SearchActivity)
  fun inject(activity: ActionsActivity)
}