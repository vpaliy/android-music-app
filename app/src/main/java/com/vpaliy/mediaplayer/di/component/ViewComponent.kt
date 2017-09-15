package com.vpaliy.mediaplayer.di.component

import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.ui.home.history.HistoryFragment
import com.vpaliy.mediaplayer.ui.home.loved.LovedFragment
import dagger.Component
import com.vpaliy.mediaplayer.di.scope.ViewScope

@ViewScope
@Component(dependencies = arrayOf(ApplicationComponent::class),
        modules = arrayOf(PresenterModule::class))
interface ViewComponent{
    fun inject(fragment:LovedFragment)
    fun inject(fragment:HistoryFragment)
}