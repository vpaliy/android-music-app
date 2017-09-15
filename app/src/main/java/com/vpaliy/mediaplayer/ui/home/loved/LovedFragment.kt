package com.vpaliy.mediaplayer.ui.home.loved

import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter
import javax.inject.Inject
import com.vpaliy.mediaplayer.di.qualifier.Loved

class LovedFragment : BaseFragment(), HomeContract.View {

    private lateinit var presenter:Presenter

    override fun show(list: List<Track>) {
    }

    override fun error() {

    }

    override fun empty() {

    }

    override fun setLoading(isLoading: Boolean) {

    }

    override fun layoutId()=R.layout.fragment_home

    @Inject
    override fun attach(@Loved presenter: Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }
}
