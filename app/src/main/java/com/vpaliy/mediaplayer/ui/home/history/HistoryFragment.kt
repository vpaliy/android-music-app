package com.vpaliy.mediaplayer.ui.home.history

import com.vpaliy.mediaplayer.FitnessSound
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment
import com.vpaliy.mediaplayer.di.qualifier.History
import javax.inject.Inject

class HistoryFragment : HomeFragment(){

    override fun show(list: List<Track>) {
        adapter.set(list.toMutableList())
    }

    @Inject
    override fun attach(@History presenter: Presenter) {
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
