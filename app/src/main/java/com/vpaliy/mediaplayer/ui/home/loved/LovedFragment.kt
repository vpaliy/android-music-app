package com.vpaliy.mediaplayer.ui.home.loved

import com.vpaliy.mediaplayer.FlashApp
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.qualifier.Loved

class LovedFragment : HomeFragment() {
    @Inject
    override fun attach(@Loved presenter: Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }

    override fun inject() {
        DaggerViewComponent.builder()
                .applicationComponent(FlashApp.app().component())
                .presenterModule(PresenterModule())
                .build().inject(this)
    }

    override fun emptyMessage(): Int =R.string.empty_liked
    override fun alertMessage():String =
            getString(R.string.loved_alert)
}
