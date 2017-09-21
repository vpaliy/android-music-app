package com.vpaliy.mediaplayer.ui.details

import com.vpaliy.mediaplayer.R
import com.vpaliy.mediaplayer.ui.base.BaseFragment
import com.vpaliy.mediaplayer.ui.details.ActionsContract.Presenter
import javax.inject.Inject

class ActionsFragment: BaseFragment(),ActionsContract.View{

    private lateinit var presenter: Presenter

    override fun layoutId()= R.layout.fragment_actions

    override fun added()=showMessage(R.string.removed_message)
    override fun removed()=showMessage(R.string.removed_message)
    override fun liked()=showMessage(R.string.removed_message)
    override fun disliked()=showMessage(R.string.removed_message)
    override fun error()=showMessage(R.string.removed_message)

    @Inject
    override fun attach(presenter: Presenter) {
        this.presenter=presenter
        presenter.attach(this)
    }
}