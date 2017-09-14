package com.vpaliy.mediaplayer.ui.home

import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BasePresenter
import com.vpaliy.mediaplayer.ui.base.BaseView

interface HomeContract {
    interface View : BaseView<Presenter> {
        fun show(list:List<Track>)
        fun error()
        fun empty()
    }
    interface Presenter : BasePresenter {
        override fun start()
        override fun stop()
        fun attach(view:View)
    }
}
