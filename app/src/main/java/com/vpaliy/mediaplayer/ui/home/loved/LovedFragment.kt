package com.vpaliy.mediaplayer.ui.home.loved

import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.domain.model.Track
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.home.HomeContract
import com.vpaliy.mediaplayer.ui.home.HomeContract.Presenter

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

    override fun layoutId(): Int {
        return R.layout.fragment_home
    }
    override fun attach(presenter: Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }
}
