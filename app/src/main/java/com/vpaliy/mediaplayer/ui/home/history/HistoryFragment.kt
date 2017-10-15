package com.vpaliy.mediaplayer.ui.home.history

import com.vpaliy.mediaplayer.FlashApp
import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.di.component.DaggerViewComponent
import com.vpaliy.mediaplayer.di.module.PresenterModule
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import com.vpaliy.mediaplayer.ui.home.HomeFragment
import com.vpaliy.mediaplayer.di.qualifier.History
import javax.inject.Inject

class HistoryFragment : HomeFragment(){
    @Inject
    override fun attach(@History presenter: Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }

    override fun inject() {
        DaggerViewComponent.builder()
                .applicationComponent(FlashApp.app().component())
                .presenterModule(PresenterModule())
                .build().inject(this)
    }

    override fun emptyMessage(): Int =R.string.empty_history
    override fun alertMessage():String = getString(R.string.history_alert)
}
