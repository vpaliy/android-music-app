package com.vpaliy.mediaplayer.di.component

import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.ui.home.history.HistoryFragment
import com.vpaliy.mediaplayer.ui.home.loved.LovedFragment
import com.vpaliy.mediaplayer.ui.search.SearchActivity
import dagger.Component
import com.vpaliy.mediaplayer.di.scope.ViewScope
import com.vpaliy.mediaplayer.ui.details.ActionsActivity

@ViewScope
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(PresenterModule::class))
interface ViewComponent{
    fun inject(fragment:LovedFragment)
    fun inject(fragment:HistoryFragment)
    fun inject(activity: SearchActivity)
    fun inject(activity:ActionsActivity)
}