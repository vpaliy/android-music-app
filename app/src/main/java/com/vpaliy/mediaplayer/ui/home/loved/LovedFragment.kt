package com.vpaliy.mediaplayer.ui.home.loved

import com.vpaliy.mediaplayer.FitnessSound
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.qualifier.Loved

class LovedFragment : HomeFragment() {

    override fun show(list: List<Track>) {
    }

    @Inject
    override fun attach(@Loved presenter: Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }

    override fun inject() {
        DaggerViewComponent.builder()
                .applicationComponent(FitnessSound.app().component())
                .presenterModule(PresenterModule())
                .build().inject(this)
    }
}
